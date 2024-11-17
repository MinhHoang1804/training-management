package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ScheduleResponse {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheDuleDetailsInfo {
        private Integer sessionId;
        private Integer sessionOrder;
        private String lesson;
        private String description;
        private LocalDate date;
    }
}
