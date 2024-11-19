package com.g96.ftms.service.schedule;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ScheduleResponse;
import com.g96.ftms.entity.Session;

import java.time.LocalDate;
import java.util.List;

public interface IScheduleService {
    public ApiResponse<List<ScheduleResponse.ScheDuleDetailsInfo>> generateTimeTable(LocalDate startDate,Integer slot, List<Session> sessions);
}
