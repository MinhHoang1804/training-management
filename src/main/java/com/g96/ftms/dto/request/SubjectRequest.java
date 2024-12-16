package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import com.g96.ftms.dto.response.SchemeResponse;
import lombok.AllArgsConstructor;
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
//        private String documentLink;
        private String descriptions;
        private boolean status;
        List<SubjectSchemeAddRequest>schemes;
        List<SubjectLessonRequest>lessonList;
    }
    @Data
    @NoArgsConstructor
    public static class SubjectEditRequest extends SubjectAddRequest{
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectSchemeAddRequest {
        private Long markSchemeId;
        private String markName;
        private Double markWeight;

        public SubjectSchemeAddRequest(String markName, Double markWeight) {
            this.markName = markName;
            this.markWeight = markWeight;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SubjectLessonRequest{
        private String lesson;
        private Integer sessionOrder;
        private String description;
    }

}
