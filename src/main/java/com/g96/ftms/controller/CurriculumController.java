package com.g96.ftms.controller;

import com.g96.ftms.dto.CurriculumDTO;
import com.g96.ftms.dto.request.CurriculumRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.curriculum.ICurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/curriculums")
public class CurriculumController {
    private final ICurriculumService curriculumService;
    @PostMapping("/search")
    public ApiResponse<?> search(@RequestBody CurriculumRequest.CurriculumPagingRequest model) {
        return curriculumService.search(model);
    }

    @GetMapping("/detail/{curriculumId}")
    public ApiResponse<?> getCurriculumDetails(@PathVariable Long curriculumId) {
      return curriculumService.getCurriculumDetail(curriculumId);
    }

    // Chỉ system admin và coordinator có thể chỉnh sửa
    @PutMapping("/update")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COORDINATOR')")
    public  ApiResponse<?> updateCurriculum(@RequestBody CurriculumRequest.CurriculumEditRequest model) {
        return curriculumService.updateCurriculum(model);
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COORDINATOR')")  // Chỉ cho phép admin và coordinator tạo mới
    public ApiResponse<?> createCurriculum(@RequestBody  CurriculumRequest.CurriculumAddRequest model) {
       return curriculumService.createCurriculum(model);
    }
}
