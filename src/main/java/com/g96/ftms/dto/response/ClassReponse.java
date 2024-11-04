package com.g96.ftms.dto.response;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ClassReponse {
    @Data
    @NoArgsConstructor
    @Builder
    public static class ClassInforDTO {
        private Long classId;
        private String classCode;
        private String descriptions;
        private Boolean status;
        private String admin;
        private LocalDateTime endDate = LocalDateTime.now();
        private LocalDateTime startDate = LocalDateTime.now();
        private LocalDateTime createdDate = LocalDateTime.now();
    }
}
