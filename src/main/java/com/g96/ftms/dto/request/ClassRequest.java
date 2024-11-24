package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import com.g96.ftms.dto.response.ScheduleResponse;
import com.g96.ftms.dto.response.SessionResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ClassRequest {
    @Data
    @NoArgsConstructor
    public static class ClassPagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
    }
    @Data
    @NoArgsConstructor
    public static class ClassAddRequest {
        private String classCode;
        private Long curriculumId;
        private String admin;
        private String description;
        private Integer planTraineeNo;
        private String supplier;
    }

    @Data
    @NoArgsConstructor
    public static class UpdateClassByAdminForm {
        private Long classId;
        private Long locationId;
        private Long curriculumId;
        private Long generationId;
        private String description;
        private LocalDateTime endDate ;
        private LocalDateTime startDate;
        private List<SubjectTraineeDto>subjectList;
        private List<SubjectSessionDto>subjectSessionList;
    }

    @Data
    @NoArgsConstructor
    public static class SubjectTraineeDto {
        private String trainer;
        private Long subjectId;
        private Integer slot;
    }
    @Data
    @NoArgsConstructor
    public static class SubjectSessionDto {
        private Long subjectId;
        private List<ScheduleResponse.ScheDuleDetailsInfo>sessionList;
    }

    @Data
    @NoArgsConstructor
    public static class UpdateStatusClassFrom {
        private Long classId;
    }


}
