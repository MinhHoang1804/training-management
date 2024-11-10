package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SchemeRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SchemeClassSubjectRequest  {
        private Long subjectId;
    }
}
