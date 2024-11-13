package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class TraineeResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TraineeInfoDTO {
        private Long userId;
        private String account;
        private String fullName;
        private String className;
        private String email;
        private String phone;
    }
}
