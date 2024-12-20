package com.g96.ftms.controller;

import com.g96.ftms.dto.request.GradeRequest;
import com.g96.ftms.dto.request.SchemeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.grade.IGradeService;
import com.g96.ftms.service.markscheme.IMarkSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grade-management")
@RequiredArgsConstructor
public class GradeController {
    private final IGradeService gradeService;
    private final IMarkSchemeService markSchemeService;
    @PostMapping("/search")
    public ApiResponse<?> getGradeList(@RequestBody GradeRequest.GradePagingRequest model) {
        return gradeService.search(model);
    }
    @PostMapping("/subject-grade-detail")
    public ApiResponse<?> getSubjectGradeDetail(@RequestBody GradeRequest.GradedSubjectRequest model) {
        return gradeService.getSubjectGradeDetail(model);
    }

    @PostMapping("/add-grade-trainee")
    public ApiResponse<?> addGrade(@RequestBody List<GradeRequest.GradedSubjectAddRequest> model) {
        return gradeService.addGradeForTrainee(model);
    }

    @PostMapping("/save-grade-setting")
    public ApiResponse<?> saveGradeSetting(@RequestBody GradeRequest.GradeSettingUpdateRequest model) {
        return gradeService.saveGradeSetting(model);
    }

    @PostMapping("/get-scheme-in-subject-class")
    public ApiResponse<?> getSchemeInSubjectClass(@RequestBody SchemeRequest.SchemeClassSubjectRequest model) {
        return markSchemeService.getSchemeInSubjectClass(model);
    }

    @PostMapping("/search-trainee-pass")
    public ApiResponse<?> getTraineePassList(@RequestBody GradeRequest.GradePagingRequest model) {
        return gradeService.searchTraineePassed(model);
    }

    @PostMapping("/get-trainee-pass")
    public ApiResponse<?> getTraineePass(@RequestBody GradeRequest.UserGradePassRequest model) {
        return gradeService.getGradeSumary(model.getUserName(),model.getClassId());
    }

    @PostMapping("/get-grade-trainee")
    public ApiResponse<?> getGradeTrainee(@RequestBody GradeRequest.UserGradePassRequest model) {
        return gradeService.getGradeTrainee(model.getUserName(),model.getClassId());
    }
}
