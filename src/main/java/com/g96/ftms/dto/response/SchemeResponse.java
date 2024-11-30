package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SchemeResponse {
    @Data
    @NoArgsConstructor
    public static class SubjectSchemeInfo {
        private Integer markSchemeId;
        private String markName;
        private Double markWeight;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SubjectSchemeGrade {
        private Long markSchemeId;
        private String markName;
        private Double markWeight;
        private Double grade;
    }
}
