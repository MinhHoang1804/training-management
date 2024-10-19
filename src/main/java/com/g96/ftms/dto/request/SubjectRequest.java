package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SubjectRequest {
    @Data
    @NoArgsConstructor
    public static class SubjectPagingRequest extends PagingBaseParams{
        private String keyword;
        private Boolean status;
    }
    @Data
    @NoArgsConstructor
    public static class SubjectEditRequest extends PagingBaseParams{
        private String keyword;
        private Boolean status;
    }
    @Data
    @NoArgsConstructor
    public static class SubjectAddRequest extends PagingBaseParams{
        private String keyword;
        private Boolean status;
    }
}
