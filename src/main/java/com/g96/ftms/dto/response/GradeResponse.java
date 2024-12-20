package com.g96.ftms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GradeResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class GradeInfoDTO {
        private Long userId;
        private String traineeName;
        List<GradeComponent> gradeComponentList;
        private Double total;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class GradeComponent {
        private String subjectName;
        private Double grade;
        List<SchemeResponse.SubjectSchemeGrade>gradeComponents;
        public GradeComponent(String subjectName, Double grade) {
            this.subjectName = subjectName;
            this.grade = grade;
        }
//        private Double weight;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class GradeSubjectSchemeDetail {
        private String traineeName;
        private Double grade;
        private LocalDateTime lastUpdate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class GradeSummaryInfoResponse {
       private String userName;
       private String fullName;
       private String comment;
       private Double grade;
       private Boolean isPassed;
       private String ranking;
    }

}
