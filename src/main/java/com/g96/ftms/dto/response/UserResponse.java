package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UserInfoDTO {
        private Long userId;
        private String account;
        private String fullName;
        private String email;
        private String phone;
        private Boolean status;
        private String role;
    }
}
