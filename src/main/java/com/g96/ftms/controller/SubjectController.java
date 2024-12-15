package com.g96.ftms.controller;

import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.service.subject.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {

    private final ISubjectService subjectService;

    @Autowired
    public SubjectController(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }


    //    @PreAuthorize("hasAnyRole( 'ROLE_ADMIN','ROLE_COORDINATOR','CLASS_ADMIN')")
    @PostMapping("/search")
    public ApiResponse<?> getSubjectList(@RequestBody SubjectRequest.SubjectPagingRequest model) {
        return subjectService.search(model);
    }

    @GetMapping("/subject-options")
    public ApiResponse<?> getAllSubjectOptions() {
        return subjectService.getAllSubjectOption();
    }


    @GetMapping("/detail/{id}")
    public ApiResponse<?> getSubjectDetail(@PathVariable("id") Long subjectId) {
        return subjectService.getSubjectDetail(subjectId);
    }
    @GetMapping("/get-subject-in-class/{id}")
    public ApiResponse<?> getsubjectInClass(@PathVariable("id") Long classId) {
        List<Subject> subjectInClass = subjectService.getSubjectInClass(classId);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), subjectInClass);

    }

    @PostMapping("/add-subject")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Subject> addSubject(@RequestBody SubjectRequest.SubjectAddRequest model) {
        return subjectService.addSubject(model);
    }

    @PutMapping("/update-subject")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Subject> updateSubject(@RequestBody SubjectRequest.SubjectEditRequest model) {
        return subjectService.updateSubject(model);
    }


    @GetMapping("/check-update/{id}")
    public ApiResponse<?> checkUpdate(@PathVariable("id") Long subjectId) {
        return subjectService.checkUpdate(subjectId);
    }
}
