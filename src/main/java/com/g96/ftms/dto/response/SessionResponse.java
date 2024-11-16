package com.g96.ftms.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class SessionResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class SessionInfoDTO {
        private Long sessionId;
        private Integer sessionOrder;
        private String lesson;
        private String description;
    }
}
