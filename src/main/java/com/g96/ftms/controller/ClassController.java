package com.g96.ftms.controller;

import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.classes.IClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class-management")
@RequiredArgsConstructor
public class ClassController {
    private final IClassService classService;

    @GetMapping("/search")
    public ApiResponse<?> getClassList(@RequestBody ClassRequest.ClassPagingRequest model) {
        return classService.search(model);
    }
    @GetMapping("/detail/{id}")
    public ApiResponse<?> getClass(@PathVariable("id") Long classId) {
        return classService.getClassDetail(classId);
    }
    @PostMapping("/info-trainee/{id}")
    public ApiResponse<?> getInfoForTrainee(@PathVariable("id") Long classId) {
        return classService.getClassForTrainee(classId);
    }

    @PostMapping("/add")
    public ApiResponse<?> addClass(@RequestBody ClassRequest.ClassAddRequest model) {
        return classService.addClass(model);
    }
}
