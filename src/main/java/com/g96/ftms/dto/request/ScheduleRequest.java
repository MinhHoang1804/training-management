package com.g96.ftms.dto.request;

import com.g96.ftms.entity.Session;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ScheduleRequest {
    @Data
    @NoArgsConstructor
    public static class GradedSubjectRequest {
        LocalDate startDate;
        List<Session> sessions;
    }
}
