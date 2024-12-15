package com.g96.ftms.controller;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.generation.IGenerationService;
import com.g96.ftms.service.location.IlocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/generation-management")
@RequiredArgsConstructor
public class GenerationController {
    private final IGenerationService generationService;

    @GetMapping("/get-all-generation")
    public ApiResponse<?> getAllLocation() {
        return generationService.getAllGenerations();
    }
}
