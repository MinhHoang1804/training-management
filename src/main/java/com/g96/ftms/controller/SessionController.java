package com.g96.ftms.controller;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.session.ISessionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
