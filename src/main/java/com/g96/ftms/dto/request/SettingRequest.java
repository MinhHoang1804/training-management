package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import com.g96.ftms.util.constants.SettingGroupEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SettingRequest {
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class SettingPagingRequest extends PagingBaseParams {
            private String keyword;
            private Boolean status;
        }
}
