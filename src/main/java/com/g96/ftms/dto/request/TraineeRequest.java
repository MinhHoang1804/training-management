package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TraineeRemoveRequest {
        private Long classId;
        List<Long>listUserIds;
    }
}
