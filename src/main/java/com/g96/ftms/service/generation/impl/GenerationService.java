package com.g96.ftms.service.generation.impl;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.entity.Generation;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.GenerationRepository;
import com.g96.ftms.service.generation.IGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerationService implements IGenerationService {
    private final GenerationRepository generationRepository;
    @Override
    public ApiResponse<?> getAllGenerations() {
        List<Generation> generationList = generationRepository.findAll();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), generationList);

    }
}
