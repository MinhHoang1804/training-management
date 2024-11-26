package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AttendanceServiceRequest {
    @Data
    @NoArgsConstructor
    public static class SearchRequest {
        private Long subjectId;
        private Long classId;
        private LocalDateTime date;
    }

    @Data
    @NoArgsConstructor
    public static class SearchByUserRequest extends SearchRequest {
        private Long userId;
    }
}
