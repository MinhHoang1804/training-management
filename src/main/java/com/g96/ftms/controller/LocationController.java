package com.g96.ftms.controller;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.location.IlocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/location-management")
@RequiredArgsConstructor
public class LocationController {
    private final IlocationService locationService;
    @GetMapping("/get-all-location")
    public ApiResponse<?> getAllLocation() {
        return locationService.getAllLocation();
    }

}
