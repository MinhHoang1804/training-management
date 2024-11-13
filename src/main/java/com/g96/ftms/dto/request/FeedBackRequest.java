package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import com.g96.ftms.entity.Questions;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class FeedBackRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeedBackPagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
        private Long userId;
        private Long classId;
        private Long subjectId;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeedBackAddRequest extends PagingBaseParams {
        private Long feedBackId;
        private Long questionId;
        private Long classId;
        private Long subjectId;
        private Long userId;
        private LocalDateTime openTime;
        private LocalDateTime feedBackDate;
        private String description;
        List<QuestionAnswerFormRequest>listAnswers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeedBackDetailFormRequest{
        private Long feedBackId;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class QuestionAnswerFormRequest{
        Long questionId;
        String answer;
    }
}
