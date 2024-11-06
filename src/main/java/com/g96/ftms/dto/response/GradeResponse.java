package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GradeResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class GradeInfoDTO {
        private Long userId;
        private String traineeName;
        List<GradeComponent> gradeComponentList;
        private Double total;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class GradeComponent {
        private String name;
        private Double grade;
    }
}
