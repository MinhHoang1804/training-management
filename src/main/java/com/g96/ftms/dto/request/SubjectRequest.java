package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import com.g96.ftms.dto.response.SchemeResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class SubjectRequest {
    @Data
    @NoArgsConstructor
    public static class SubjectPagingRequest extends PagingBaseParams{
        private String keyword;
        private Boolean status;
    }
    @Data
    @NoArgsConstructor
    public static class SubjectAddRequest{
        private String subjectName;
        private String subjectCode;
        private String documentLink;
        private String descriptions;
        private boolean status;
        List<SchemeResponse.SubjectSchemeInfo>schemes;
    }
    @Data
    @NoArgsConstructor
    public static class SubjectEditRequest extends SubjectAddRequest{
        private Long id;
    }

}
