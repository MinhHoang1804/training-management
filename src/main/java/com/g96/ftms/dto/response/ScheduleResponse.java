package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ScheduleResponse {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheDuleDetailsInfo {
        private Long sessionId;
        private Integer sessionOrder;
        private String lesson;
        private String description;
        private LocalDateTime date;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TimeSlotInfo {
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        public static TimeSlotInfo getTimeSlot(int slot) {
            LocalDate today = LocalDate.now();
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;

            switch (slot) {
                case 1:
                    startDateTime = today.atTime(LocalTime.of(7, 30));
                    endDateTime = today.atTime(LocalTime.of(9, 0));
                    break;
                case 2:
                    startDateTime = today.atTime(LocalTime.of(9, 30));
                    endDateTime = today.atTime(LocalTime.of(11, 0));
                    break;
                case 3:
                    startDateTime = today.atTime(LocalTime.of(13, 0)); // 1h chiều
                    endDateTime = today.atTime(LocalTime.of(14, 30));
                    break;
                case 4:
                    startDateTime = today.atTime(LocalTime.of(15, 0)); // 3h chiều
                    endDateTime = today.atTime(LocalTime.of(16, 30));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid slot: " + slot);
            }

            return new TimeSlotInfo(startDateTime, endDateTime);
        }
    }
}
