package com.g96.ftms.controller;

import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.trainee.ITraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainee")
public class TraineeController {
    private final ITraineeService traineeService;
    @PostMapping("/search")
    public ApiResponse<?> getSubjectList(@RequestBody TraineeRequest.TraineePagingRequest model) {
        return traineeService.search(model);
    }
}
