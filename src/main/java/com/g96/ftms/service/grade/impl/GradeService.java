package com.g96.ftms.service.grade.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.GradeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.GradeResponse;
import com.g96.ftms.entity.Grade;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.entity.User;
import com.g96.ftms.entity.UserClassRelation;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.GradeRepository;
import com.g96.ftms.repository.UserClassRelationRepository;
import com.g96.ftms.repository.UserRepository;
import com.g96.ftms.service.grade.IGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        // Tính tổng điểm của người dùng này qua tất cả các môn học
        double total = subjectTotals.values().stream().mapToDouble(Double::doubleValue).sum();

        // Trả về đối tượng GradeInfoDTO chứa thông tin của người dùng và danh sách điểm theo môn học
        return GradeResponse.GradeInfoDTO.builder()
                .userId(user.getUserId())
                .traineeName(user.getFullName())
                .gradeComponentList(gradeComponents)
                .total(total)
                .build();
    }
}
