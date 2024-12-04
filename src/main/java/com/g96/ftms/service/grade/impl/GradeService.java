package com.g96.ftms.service.grade.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.GradeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.GradeResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.entity.Class;
import com.g96.ftms.entity.*;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.grade.IGradeService;
import com.g96.ftms.util.SqlBuilderUtils;
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
import java.util.OptionalDouble;
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
    private final GradeSummaryRepository gradeSummaryRepository;

    @Override
    public ApiResponse<PagedResponse<GradeResponse.GradeInfoDTO>> search(GradeRequest.GradePagingRequest model) {
        List<GradeResponse.GradeInfoDTO> list = new ArrayList<>();
        // Tìm danh sách `User` trong class với `Pageable`
        Page<User> pages = userRepository.findUsersByClassId(model.getClassId(), model.getPageable());

        for (User user : pages.getContent()) {
            // Lấy danh sách điểm của `User` theo classId
            GradeResponse.GradeInfoDTO gradesByUserId = getGradesByUserId(user.getUserId(), model.getClassId());
            if (gradesByUserId != null) {
                list.add(gradesByUserId);
            }
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
        if (grade != null) {
            GradeResponse.GradeSubjectSchemeDetail response = GradeResponse.GradeSubjectSchemeDetail.builder()
                    .traineeName(grade.getUser().getFullName())
                    .grade(grade.getGrade())
                    .lastUpdate(grade.getGradeDate())
                    .build();
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
        }else{
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<?> saveGradeSetting(GradeRequest.GradeSettingUpdateRequest model) {
        List<MarkScheme> list = model.getSchemes().stream().filter(s -> s.getSchemeId() != null)
                .map(item -> {
                    return mapper.map(item, MarkScheme.class);
                }).toList();
        //save entity exist;
        markSchemeRepository.saveAll(list);
        List<MarkScheme> newList = model.getSchemes().stream().filter(s -> s.getSchemeId() == null)
                .map(item -> {
                    return mapper.map(item, MarkScheme.class);
                }).toList();
        if (!newList.isEmpty()) {
            markSchemeRepository.saveAll(newList);
        }
        //save new entity
        //remove all grade
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");
    }

    @Override
    @Transactional
    public ApiResponse<?> addGradeForTrainee(List<GradeRequest.GradedSubjectAddRequest> model) {
        List<GradeRequest.GradedSubjectAddRequest> list = new ArrayList<>();
        for (GradeRequest.GradedSubjectAddRequest item : model) {
            addGradeForTrainee(item);
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");

    }

    @Override
    public ApiResponse<GradeResponse.GradeSummaryInfoResponse> getGradeSumary(String userName, Long classId) {
        User user = userRepository.findByAccount(userName);
        if(user==null){
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }

        Class c = classRepository.findById(classId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));

        GradeSummary gradeSummary = gradeSummaryRepository.findByUser_UserIdAndClasss_ClassId(user.getUserId(), c.getClassId());
        GradeResponse.GradeSummaryInfoResponse response= GradeResponse.GradeSummaryInfoResponse.builder()
                .fullName(user.getFullName())
                .userName(user.getAccount())
                .grade(gradeSummary.getGrade())
                .comment(gradeSummary.getComment())
                .isPassed(gradeSummary.getIsPassed())
                .ranking(getRanking(gradeSummary.getGrade()))
                .build();

        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<PagedResponse<GradeResponse.GradeSummaryInfoResponse>> searchTraineePassed(GradeRequest.GradePagingRequest model) {
        String keywordFilter = SqlBuilderUtils.createKeywordFilter(model.getKeyword());
        Page<User> users = userRepository.searchUserPass(keywordFilter, model.getStatus(),model.getClassId(), model.getPageable());
        List<GradeResponse.GradeSummaryInfoResponse>list=new ArrayList<>();
        for (User u:users.getContent()){
            GradeResponse.GradeSummaryInfoResponse gradeSummary = getGradeSumary(u.getAccount(), model.getClassId()).getData();
            list.add(gradeSummary);
        }

        PagedResponse<GradeResponse.GradeSummaryInfoResponse> response = new PagedResponse<>(
                list,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    public void addGradeForTrainee(GradeRequest.GradedSubjectAddRequest model) {
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
        if (!markSchemeRepository.existsByMarkSchemeIdAndSubject_SubjectId(markScheme.getMarkSchemeId(), subject.getSubjectId())) {
            throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.SCHEME_NOT_FOUND_IN_SUBJECT);
        }
        Grade map = mapper.map(model, Grade.class);
        map.setMarkScheme(markScheme);
        map.setClasss(c);
        map.setUser(user);
        map.setSubject(subject);

        GradeId gradeId = GradeId.builder().classId(c.getClassId())
                .markSchemeId(markScheme.getMarkSchemeId())
                .userId(user.getUserId())
                .subjectId(subject.getSubjectId())
                .classId(c.getClassId())
                .build();
        map.setId(gradeId);
        gradeRepository.save(map);
    }


    // Service để lấy điểm của một người dùng
    public GradeResponse.GradeInfoDTO getGradesByUserId(Long userId, Long classId) {
        // Lấy thông tin người dùng từ danh sách điểm
        User user = userRepository.findById(userId).orElse(null);
        List<GradeResponse.GradeComponent> gradeComponents = getSchemeGrade(userId, classId);
        OptionalDouble average = gradeComponents.stream().mapToDouble(GradeResponse.GradeComponent::getGrade).average();
        double total = 0;
        if (average.isPresent()) {
            total = average.getAsDouble();
        }
        return GradeResponse.GradeInfoDTO.builder()
                .userId(user.getUserId())
                .traineeName(user.getFullName())
                .gradeComponentList(gradeComponents)
                .total(total)
                .build();
    }

    public List<GradeResponse.GradeComponent> getSchemeGrade(Long userId, Long classId) {
        List<GradeResponse.GradeComponent> list = new ArrayList<>();
        Class c = classRepository.findById(classId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        List<Subject> subjectList = c.getCurriculum().getCurriculumSubjectRelationList().stream().map(CurriculumSubjectRelation::getSubject).toList();

        //get scheme
        for (Subject subject : subjectList) {
            List<MarkScheme> markSchemeList = subject.getMarkSchemeList();
            List<SchemeResponse.SubjectSchemeGrade> schemeGradeList = new ArrayList<>();
            for (MarkScheme markScheme : markSchemeList) {
                GradeRequest.GradedSubjectRequest model = GradeRequest.GradedSubjectRequest.builder()
                        .userId(userId).classId(classId).subjectId(subject.getSubjectId()).markSchemeId(markScheme.getMarkSchemeId())
                        .build();
                GradeResponse.GradeSubjectSchemeDetail data = getSubjectGradeDetail(model).getData();
                if(data!=null){
                    SchemeResponse.SubjectSchemeGrade subjectSchemeGrade = SchemeResponse.SubjectSchemeGrade.builder()
                            .grade(data.getGrade())
                            .markWeight(markScheme.getMarkWeight())
                            .markSchemeId(markScheme.getMarkSchemeId())
                            .markName(markScheme.getMarkName())
                            .build();
                    schemeGradeList.add(subjectSchemeGrade);
                }
            }

            double avgGrade = 0;
            OptionalDouble average = schemeGradeList.stream().mapToDouble(SchemeResponse.SubjectSchemeGrade::getGrade).average();
            if (average.isPresent()) {
                avgGrade = average.getAsDouble();
            }
            GradeResponse.GradeComponent gradeComponent = GradeResponse.GradeComponent.builder()
                    .gradeComponents(schemeGradeList)
                    .subjectName(subject.getSubjectName())
                    .grade(avgGrade)
                    .build();
            list.add(gradeComponent);
        }

        return list;
    }

    public String getRanking(Double grade){
        if(grade>=9.3){
            return "A+";
        } else if (grade>=8.5) {
            return "A";
        }else if(grade>=7.2){
            return "B";
        }else if(grade>=6){
            return "C";
        }else {
            return "D";
        }
    }
}
