package com.g96.ftms.config;

import com.g96.ftms.dto.response.SubjectResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(SubjectResponse.SubjectInfoDTO.class, SubjectResponse.SubjectInfoDTO.class).addMappings(mapper ->
                mapper.skip(SubjectResponse.SubjectInfoDTO::setCurriculums)
        );
        return modelMapper;
    }
}