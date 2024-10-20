package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CurriculumRequest {
    @Data
    @NoArgsConstructor
    public static class CurriculumPagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
    }
}
