package com.g96.ftms.controller;

import com.g96.ftms.dto.CurriculumDTO;
import com.g96.ftms.service.curriculum.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/curriculums")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/detail/{curriculumId}")
    public ResponseEntity<CurriculumDTO> getCurriculumDetails(@PathVariable Long curriculumId) {
        CurriculumDTO curriculumDTO = curriculumService.getCurriculumById(curriculumId);
        return ResponseEntity.ok(curriculumDTO);
    }

    // Chỉ system admin và coordinator có thể chỉnh sửa
    @PutMapping("/update/{curriculumId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COORDINATOR')")
    public ResponseEntity<CurriculumDTO> updateCurriculum(
            @PathVariable Long curriculumId, @RequestBody CurriculumDTO curriculumDTO) {
        curriculumDTO.setCurriculumId(curriculumId);
        CurriculumDTO updatedCurriculum = curriculumService.updateCurriculum(curriculumDTO);
        return ResponseEntity.ok(updatedCurriculum);  // Trả về object đã cập nhật
    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPagedCurriculums(
            @PageableDefault(size = 10) Pageable pageable) {
        Map<String, Object> response = curriculumService.getPagedCurriculums(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COORDINATOR')")  // Chỉ cho phép admin và coordinator tạo mới
    public ResponseEntity<CurriculumDTO> createCurriculum(@RequestBody CurriculumDTO curriculumDTO) {
        CurriculumDTO createdCurriculum = curriculumService.createCurriculum(curriculumDTO);
        return ResponseEntity.status(201).body(createdCurriculum);  // Trả về status 201 (Created)
    }
}
