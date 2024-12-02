package com.g96.ftms.dto.response;

import com.g96.ftms.util.constants.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public static class UserAttendanceAllSubjectResponse {
        private Long userId;
        private String userName;
        private String fullName;
        private String className;
        List<UserAttendanceSubject> listSubjectTimeTable;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserAttendanceSubject {
        private String subjectName;
        List<AttendanceStatusResponse>listWeeklyAttendances;
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendanceStatusResponse {
        private AttendanceStatus status;
        private String attendanceNote;
        private Long scheduleDetailId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String trainer;
        private String location;
    }

}
