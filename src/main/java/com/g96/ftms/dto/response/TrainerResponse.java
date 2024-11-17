package com.g96.ftms.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TrainerResponse {
    @Data
    @NoArgsConstructor
    public static class TrainerInfoDTO {
        private String fullName;
        private Long userId;
        private String account;
        private String imgAva;
        private String email;
    }
}
