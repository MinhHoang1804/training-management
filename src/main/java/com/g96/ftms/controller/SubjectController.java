package com.g96.ftms.controller;

import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Subject;
import com.g96.ftms.service.subject.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse getSubjectDetail(@PathVariable("id") Long subjectId) {
        return subjectService.getSubjectDetail(subjectId);
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Subject> addSubject(@RequestBody SubjectRequest.SubjectAddRequest model) {
        return subjectService.addSubject(model);
    }

    @PutMapping()
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Subject> updateSubject(@RequestBody SubjectRequest.SubjectEditRequest model) {
        return subjectService.updateSubject(model);
    }
}
