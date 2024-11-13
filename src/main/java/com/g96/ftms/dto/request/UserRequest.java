package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {
    @Data
    @NoArgsConstructor
    public static class UserPagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
    }
}
