package com.g96.ftms.controller;

import com.g96.ftms.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/grade-management")
public class ClassController {
    @GetMapping("/search")
    public ApiResponse getClassList() {
        return subjectService.getSubjectDetail(subjectId);
    }
}
