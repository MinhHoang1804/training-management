package com.g96.ftms.controller;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.session.ISessionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/session-management")
@RequiredArgsConstructor
public class SessionController {
    private final ISessionService sessionService;
    @PostMapping(value = "/import-session",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> importSession(@Parameter(description = "File Excel to upload") @RequestParam("file") MultipartFile file) {
        return sessionService.importExcelFile(file);
    }
    @GetMapping("/by-subject/{subjectId}")
    public ApiResponse<?> getSubjectDetail(@PathVariable("subjectId") Long subjectId) {
        return sessionService.getSessionBySubjectId(subjectId);
    }

    @GetMapping("/export/{subjectId}")
    public ResponseEntity<byte[]> exportSessions(@PathVariable("subjectId") Long subjectId) {
        return sessionService.exportSessionsToExcel(subjectId);
    }

    @GetMapping("/export-template")
    public ResponseEntity<byte[]> exportTempalte() {
        return sessionService.exportTemplate();
    }
}
