package com.g96.ftms.controller;

import com.g96.ftms.dto.request.SubjectRequest;
import com.g96.ftms.dto.request.TraineeRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.trainee.ITraineeService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainee")
public class TraineeController {
    private final ITraineeService traineeService;
    @PostMapping("/search")
    public ApiResponse<?> getSubjectList(@RequestBody TraineeRequest.TraineePagingRequest model) {
        return traineeService.search(model);
    }

    @PostMapping(value = "/import",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> importTrainees( @RequestParam("classId") Long classId ,@Parameter(description = "File Excel to upload") @RequestParam("file") MultipartFile file) {
            return traineeService.importExcelFile(file,classId);
    }
    @PostMapping(value = "/get-trainee-not-in-class")
    public ApiResponse<?> getTraineeNotInClass() {
        return traineeService.getTraineeNotInClass();
    }

    @PostMapping(value = "/remove-trainee-by-class")
    public ApiResponse<?> removeTraineeByClass(@RequestBody TraineeRequest.TraineeRemoveRequest model) {
        return traineeService.removeTrainee(model);
    }
}
