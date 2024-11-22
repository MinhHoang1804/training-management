package com.g96.ftms.service.location.impl;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.entity.Location;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.LocationRepository;
import com.g96.ftms.service.location.IlocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService implements IlocationService {
    private final LocationRepository locationRepository;

    @Override
    public ApiResponse<?> getAllLocation() {
        List<Location> bySettingsStatus = locationRepository.findBySettings_Status(true);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), bySettingsStatus);

    }
}
