package com.g96.ftms.service.setting.impl;

import com.g96.ftms.dto.common.PagedResponse;
import com.g96.ftms.dto.request.SettingRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.SettingResponse;
import com.g96.ftms.entity.Generation;
import com.g96.ftms.entity.Location;
import com.g96.ftms.entity.Settings;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.GenerationRepository;
import com.g96.ftms.repository.LocationRepository;
import com.g96.ftms.repository.SettingsRepository;
import com.g96.ftms.service.setting.ISettingService;
import com.g96.ftms.util.constants.SettingGroupEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements ISettingService {
    private final SettingsRepository settingsRepository;
    private final ModelMapper mapper;
    private final GenerationRepository generationRepository;
    private final LocationRepository locationRepository;

    @Override
    public ApiResponse<PagedResponse<SettingResponse.SettingInfoDTO>> search(SettingRequest.SettingPagingRequest model) {
        // Tìm kiếm theo keyword và status
        Page<Settings> pages = settingsRepository.searchAndFilter(model.getKeyword(), model.getStatus(), model.getPageable());

        List<SettingResponse.SettingInfoDTO> settingInfoList = new ArrayList<>();

        // Duyệt qua các settings và lấy dữ liệu từ Location và Generation
        for (Settings setting : pages) {
            SettingResponse.SettingInfoDTO settingInfoDTO = convertSettingToInfoDto(setting);
            settingInfoList.add(settingInfoDTO);
        }
        PagedResponse<SettingResponse.SettingInfoDTO> response = new PagedResponse<>(settingInfoList, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    @Transactional
    public ApiResponse<Settings> createSetting(SettingRequest.SettingAddRequest model) {
        Settings setting = mapper.map(model, Settings.class);
        //create setting generation with group required match type
        if (model.getSettingGroup() == SettingGroupEnum.GENERATION) {
            Optional<Generation> generation = generationRepository.findByGenerationName(model.getSettingName());
            //check duplicate
            if(settingsRepository.existsByGeneration_GenerationName(model.getSettingName())){
                throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_SETTING);
            }
            if(!generation.isPresent()){
                Generation g=Generation.builder().generationName(model.getSettingName()).build();
                generationRepository.save(g);
                setting.setGeneration(g);
            }else{
                setting.setGeneration(generation.get());
            }
            settingsRepository.save(setting);
        }
        //create setting location
        if (model.getSettingGroup() == SettingGroupEnum.LOCATION) {
            //check duplicate
            if(settingsRepository.existsByLocation_LocationName(model.getSettingName())){
                throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_SETTING);
            }
            //save entity
            Optional<Location> byLocation = locationRepository.findByLocationName(model.getSettingName());
            if(!byLocation.isPresent()){
                Location location = Location.builder().locationName(model.getSettingName()).build();
                locationRepository.save(location);
                setting.setLocation(location);
            }else{
                setting.setLocation(byLocation.get());
            }
            settingsRepository.save(setting);
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), setting);
    }

    @Override
    @Transactional
    public ApiResponse<Settings> updateSetting(SettingRequest.SettingEditRequest model) {
        Settings setting = settingsRepository.findById(model.getId()).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.SETTING_NOTFOUND));
        if (model.getStatus() != null) {
            setting.setStatus(model.getStatus());
        }
        setting.setDescription(model.getDescription());
        //reset group;
//        if (setting.getGeneration() != null) {
//            setting.setGeneration(null);
//            settingsRepository.save(setting); //remove generation
//        }
//        if (setting.getLocation() != null) {
//            setting.setLocation(null);
//            settingsRepository.save(setting); //remove generation
//        }

        //update group
        if (model.getSettingGroup() == SettingGroupEnum.GENERATION) {
            if(settingsRepository.existsByGeneration_GenerationNameAndSettingIdNot(model.getSettingName(), model.getId())){
                throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_SETTING);
            }
            Generation generation = setting.getGeneration();
            generation.setGenerationName(model.getSettingName());
            generationRepository.save(generation);
            setting.setDescription(model.getDescription());
            setting.setStatus(model.getStatus());
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), setting);
        }
        //save setting location
        if (model.getSettingGroup() == SettingGroupEnum.LOCATION) {
           if(settingsRepository.existsByLocation_LocationNameAndSettingIdNot(model.getSettingName(),model.getId())){
               throw new AppException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_SETTING);
           }
           Location location = setting.getLocation();
           location.setLocationName(model.getSettingName());
           locationRepository.save(location);
           setting.setDescription(model.getDescription());
           setting.setStatus(model.getStatus());
            return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), setting);
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), setting);
    }

    @Override
    public ApiResponse<SettingResponse.SettingInfoDTO> getDetail(Long id) {
        Settings setting = settingsRepository.findById(id).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, ErrorCode.SETTING_NOTFOUND));
        SettingResponse.SettingInfoDTO response = convertSettingToInfoDto(setting);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }
    public SettingResponse.SettingInfoDTO convertSettingToInfoDto(Settings setting) {
        // Duyệt qua các settings và lấy dữ liệu từ Location và Generation
        if (setting.getLocation() != null) {
            SettingResponse.SettingInfoDTO dto = new SettingResponse.SettingInfoDTO();
            dto.setId(setting.getSettingId());
            dto.setSettingName(setting.getLocation().getLocationName());
            dto.setSettingGroup(SettingGroupEnum.LOCATION.name());
            dto.setDescription(setting.getDescription());
            dto.setStatus(setting.getStatus());
            return dto;
        }


        // Duyệt qua các Generation
        if (setting.getGeneration() != null) {
            SettingResponse.SettingInfoDTO dto = new SettingResponse.SettingInfoDTO();
            dto.setId(setting.getSettingId());
            dto.setSettingName(setting.getGeneration().getGenerationName());
            dto.setSettingGroup(SettingGroupEnum.GENERATION.name());
            dto.setDescription(setting.getDescription());
            dto.setStatus(setting.getStatus());
            return dto;
        }
        return null;
    }
}
