package com.g96.ftms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SessionRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SessionEditRequest{
        private Long sessionId;
        private Integer sessionOrder;
        private String lesson;
        private String description;
    }
}
