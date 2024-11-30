package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GradeRequest {
    @Data
    @NoArgsConstructor
    public static class GradePagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
        private Long classId;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GradedSubjectRequest {
        private Long userId;
        private Long classId;
        private Long subjectId;
        private Long markSchemeId;
    }
    @Data
    @NoArgsConstructor
    public static class GradedSubjectAddRequest {
        private String user;
        private Long classId;
        private Long subjectId;
        private Long markSchemeId;
        private Double grade;
    }
    @Data
    @NoArgsConstructor
    public static class GradeSettingUpdateRequest {
       List<GradeSettingUpdateInfo> schemes;
    }

    @Data
    @NoArgsConstructor
    public static class GradeSettingUpdateInfo {
        private Long schemeId;
        private LocalDateTime dateLock;
        private String markName;
        private Double markWeight;
    }
}
