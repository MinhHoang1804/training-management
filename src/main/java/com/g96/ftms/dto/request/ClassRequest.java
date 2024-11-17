package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ClassRequest {
    @Data
    @NoArgsConstructor
    public static class ClassPagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
    }
    @Data
    @NoArgsConstructor
    public static class ClassAddRequest {
        private String classCode;
        private Long locationId;
        private Long curriculumId;
        private String admin;
        private String description;
//        private Boolean status;
        private LocalDateTime endDate ;
        private LocalDateTime startDate;
        private Integer planTraineeNo;
    }
}
