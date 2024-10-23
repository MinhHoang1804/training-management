package com.g96.ftms.service.setting.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SettingRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SettingResponse;
import com.g96.ftms.entity.Curriculum;
import com.g96.ftms.entity.Generation;
import com.g96.ftms.entity.Room;
import com.g96.ftms.entity.Settings;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.SettingsRepository;
import com.g96.ftms.service.setting.ISettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingService implements ISettingService {
    private final SettingsRepository settingsRepository;
    @Override
    public ApiResponse<PagedResponse<SettingResponse.SettingInfoDTO>> search(SettingRequest.SettingPagingRequest model) {
        // Tìm kiếm theo keyword và status
        Page<Settings> pages  = settingsRepository.searchAndFilter(model.getKeyword(), model.getStatus(), model.getPageable());

        List<SettingResponse.SettingInfoDTO> settingInfoList = new ArrayList<>();

        // Duyệt qua các settings và lấy dữ liệu từ Room và Generation
        for (Settings setting : pages ) {
            // Duyệt qua các Room
            for (Room room : setting.getRooms()) {
                SettingResponse.SettingInfoDTO dto = new SettingResponse.SettingInfoDTO();
                dto.setSettingName(room.getRoomName());
                dto.setSettingGroup("room");
                dto.setDescription(setting.getDescription());
                dto.setStatus(setting.getStatus());
                settingInfoList.add(dto);
            }

            // Duyệt qua các Generation
            for (Generation generation : setting.getGenerations()) {
                SettingResponse.SettingInfoDTO dto = new SettingResponse.SettingInfoDTO();
                dto.setSettingName(generation.getGenerationName());
                dto.setSettingGroup("generation");
                dto.setDescription(setting.getDescription());
                dto.setStatus(setting.getStatus());
                settingInfoList.add(dto);
            }
        }
        PagedResponse<SettingResponse.SettingInfoDTO> response = new PagedResponse<>(settingInfoList, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }
}
