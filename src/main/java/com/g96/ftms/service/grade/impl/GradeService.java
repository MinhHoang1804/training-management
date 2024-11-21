package com.g96.ftms.service.grade.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.GradeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.GradeResponse;
import com.g96.ftms.entity.Class;
import com.g96.ftms.entity.*;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.grade.IGradeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService implements IGradeService {
    private final UserClassRelationRepository userClassRelationRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final MarkSchemeRepository markSchemeRepository;
    private final ModelMapper mapper;
    @Override
    public ApiResponse<PagedResponse<GradeResponse.GradeInfoDTO>> search(GradeRequest.GradePagingRequest model) {
        List<GradeResponse.GradeInfoDTO> list = new ArrayList<>();

        // Tìm danh sách `User` trong class với `Pageable`
        Page<User> pages = userRepository.findUsersByClassId(model.getClassId(), model.getPageable());

        for (User user : pages.getContent()) {
            // Lấy danh sách điểm của `User` theo classId
            GradeResponse.GradeInfoDTO gradesByUserId = getGradesByUserId(user.getUserId(), model.getClassId());
            list.add(gradesByUserId);
        }

        // Tạo `Page<GradeInfoDTO>` từ danh sách `list`
        Page<GradeResponse.GradeInfoDTO> gradeInfoPage = new PageImpl<>(list, model.getPageable(), pages.getTotalElements());

        // Đóng gói `PagedResponse` từ `Page<GradeInfoDTO>`
        PagedResponse<GradeResponse.GradeInfoDTO> response = new PagedResponse<>(
                gradeInfoPage.getContent(),
                gradeInfoPage.getNumber(),
                gradeInfoPage.getSize(),
                gradeInfoPage.getTotalElements(),
                gradeInfoPage.getTotalPages(),
                gradeInfoPage.isLast()
        );

        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<GradeResponse.GradeSubjectSchemeDetail> getSubjectGradeDetail(GradeRequest.GradedSubjectRequest model) {
        Grade grade = gradeRepository.findByUser_UserIdAndClasss_ClassIdAndSubject_SubjectIdAndMarkScheme_MarkSchemeId(model.getUserId(), model.getClassId(), model.getSubjectId(), model.getMarkSchemeId());
        GradeResponse.GradeSubjectSchemeDetail response = GradeResponse.GradeSubjectSchemeDetail.builder()
                .traineeName(grade.getUser().getFullName())
                .grade(grade.getGrade())
                .lastUpdate(grade.getGradeDate())
                .build();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), null);
    }

    @Override
    @Transactional
    public ApiResponse<?> saveGradeSetting(GradeRequest.GradeSettingUpdateRequest model) {
        List<MarkScheme> list = model.getSchemes().stream().filter(s -> s.getSchemeId() != null)
                .map(item->{
                   return mapper.map(item, MarkScheme.class);
                }).toList();
        //save entity exist;
        markSchemeRepository.saveAll(list);
        List<MarkScheme> newList = model.getSchemes().stream().filter(s -> s.getSchemeId() == null)
                .map(item->{
                    return mapper.map(item, MarkScheme.class);
                }).toList();
        if(!newList.isEmpty()){
            markSchemeRepository.saveAll(newList);
        }
        //save new entity
        //remove all grade
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");
    }

    @Override
    public ApiResponse<?> addGradeForTrainee(GradeRequest.GradedSubjectAddRequest model) {
        User user = userRepository.findByAccount(model.getUser());
        if (user == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
        MarkScheme markScheme = markSchemeRepository.findById(model.getMarkSchemeId()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.SCHEME_NOT_FOUND));
        Class c = classRepository.findById(model.getClassId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        Subject subject = subjectRepository.findById(model.getSubjectId()).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.SUBJECT_NOT_FOUND));
        //check markschme in subject
        if(!markSchemeRepository.existsByMarkSchemeIdAndSubject_SubjectId(markScheme.getMarkSchemeId(),subject.getSubjectId())){
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.SCHEME_NOT_FOUND_IN_SUBJECT);
        }
        Grade map = mapper.map(model, Grade.class);
        map.setMarkScheme(markScheme);
        map.setClasss(c);
        map.setUser(user);
        map.setSubject(subject);

        GradeId gradeId=GradeId.builder().classId(c.getClassId())
                .markSchemeId(markScheme.getMarkSchemeId())
                .userId(user.getUserId())
                .subjectId(subject.getSubjectId())
                .classId(c.getClassId())
                .build();
        map.setId(gradeId);
        gradeRepository.save(map);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");
    }


    // Service để lấy điểm của một người dùng
    public GradeResponse.GradeInfoDTO getGradesByUserId(Long userId,Long classId) {
        // Lấy danh sách điểm của người dùng (userId)
        List<Grade> userGrades = gradeRepository.findByUser_UserIdAndClasss_ClassId(userId,classId);

        if (userGrades.isEmpty()) {
            return null; // Hoặc có thể trả về ngoại lệ tùy theo yêu cầu
        }

        // Lấy thông tin người dùng từ danh sách điểm
        User user = userRepository.findById(userId).orElse(null);

        // Nhóm các bản ghi `Grade` theo `subject` và tính tổng điểm theo trọng số cho từng môn học
        Map<String, Double> subjectTotals = userGrades.stream()
                .collect(Collectors.groupingBy(
                        grade -> grade.getSubject().getSubjectName(),
                        Collectors.summingDouble(grade -> grade.getGrade() * grade.getMarkScheme().getMarkWeight())
                ));

        // Chuyển đổi Map thành danh sách `GradeComponent`, mỗi `GradeComponent` đại diện cho một môn học
        List<GradeResponse.GradeComponent> gradeComponents = subjectTotals.entrySet().stream()
                .map(entry -> new GradeResponse.GradeComponent(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Tính tổng điểm trung bình của người dùng này qua tất cả các môn học
        double total = subjectTotals.values().stream().mapToDouble(Double::doubleValue).sum()/subjectTotals.size();

        // Trả về đối tượng GradeInfoDTO chứa thông tin của người dùng và danh sách điểm theo môn học
        return GradeResponse.GradeInfoDTO.builder()
                .userId(user.getUserId())
                .traineeName(user.getFullName())
                .gradeComponentList(gradeComponents)
                .total(total)
                .build();
    }
}
