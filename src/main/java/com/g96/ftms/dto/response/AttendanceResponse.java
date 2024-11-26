package com.g96.ftms.dto.response;

import com.g96.ftms.util.constants.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AttendanceResponse {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchResponse {
        List<UserAttendanceResponse>listAttendances;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserAttendanceResponse {
        private Long userId;
        private String fullName;
        List<AttendanceStatusResponse> litAttendanceStatuses;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendanceStatusResponse {
        private AttendanceStatus status;
        private String attendanceNote;
    }
}
