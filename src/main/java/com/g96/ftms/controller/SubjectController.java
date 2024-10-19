package com.g96.ftms.controller;

import com.g96.ftms.dto.SubjectDTO;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.g96.ftms.entity.Subject;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole( 'ROLE_ADMIN','ROLE_COORDINATOR','CLASS_ADMIN')")
    public ApiResponse getSubjectList() {
        return subjectService.getAllSubjectsWithCurriculum();
    }

    @GetMapping("/detail/{id}")
    public ApiResponse getSubjectDetail(@PathVariable("id") Long subjectId) {
        return subjectService.getSubjectDetail(subjectId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<SubjectDTO> addSubject(@RequestBody SubjectDTO subjectDTO) {
        return subjectService.addSubject(subjectDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Subject> updateSubject(@PathVariable("id") Long subjectId, @RequestBody Subject subject) {
        return subjectService.updateSubject(subjectId, subject);
    }
}
