package com.g96.ftms.dto.request;

import com.g96.ftms.util.constants.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AttendanceServiceRequest {
    @Data
    @NoArgsConstructor
    public static class SearchRequest {
        private Long subjectId;
        private Long classId;
    }

    @Data
    @NoArgsConstructor
    public static class SearchByUserRequest  {
        private Long classId;
        private String userName;
    }

    @Data
    @NoArgsConstructor
    public static class AttendanceUserEditRequest {
        private List<AttendanceUserStatus> data;
    }

    @Data
    @NoArgsConstructor
    public static class AttendanceUserStatus {
        private AttendanceStatus status;
        private String attendanceNote;
        private Long scheduleDetailId;
        private Long userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendanceUserReportRequest {
        private Long userId;
        private Long classId;
        private Long subjectId;
    }

}
