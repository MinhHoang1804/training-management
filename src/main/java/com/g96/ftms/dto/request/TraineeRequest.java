package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TraineeRequest {
    @Data
    @NoArgsConstructor
    public static class TraineePagingRequest extends PagingBaseParams {
        private Long classId;
        private String keyword;
        private Boolean status;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TraineeAddRequest {
        private String account;
    }
}
