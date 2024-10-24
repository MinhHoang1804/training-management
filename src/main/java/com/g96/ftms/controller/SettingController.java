package com.g96.ftms.controller;

import com.g96.ftms.dto.request.SettingRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.setting.ISettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings")
public class SettingController {

    private final ISettingService settingService;

    @PostMapping("/search")
    public ApiResponse<?> search(@RequestBody SettingRequest.SettingPagingRequest model) {
        return settingService.search(model);
    }
    @PostMapping("/create")
    public ApiResponse<?> createSetting(@RequestBody SettingRequest.SettingAddRequest model) {
        return settingService.createSetting(model);
    }
}
