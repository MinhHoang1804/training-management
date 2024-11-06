package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GradeRequest {
    @Data
    @NoArgsConstructor
    public static class GradePagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
        private Long classId;
    }
}
