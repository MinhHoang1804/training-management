package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FeedBackResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class FeedBackInfoDTO {
        private Long feedbackId;
        private Long userId;
        private String traineeName;
        private String subjectCode;
        private Double avgRating;
        private LocalDateTime openDate;
        private LocalDateTime lastUpdate;
    }
}
