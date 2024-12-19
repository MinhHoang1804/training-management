package com.g96.ftms.dto.request;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TrainerRequest {
    @Data
    @NoArgsConstructor
    public static class TrainerForClassRequest {
        @Valid
        LocalDateTime startDate;

        @Valid
        LocalDateTime endDate;
    }

}
