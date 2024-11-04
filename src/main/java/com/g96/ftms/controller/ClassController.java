package com.g96.ftms.controller;

import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.classes.IClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/grade-management")
@RequiredArgsConstructor
public class ClassController {
    private final IClassService classService;

    @GetMapping("/search")
    public ApiResponse<?> getClassList(@RequestBody ClassRequest.ClassPagingRequest model) {
        return classService.search(model);
    }
}
