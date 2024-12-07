package com.g96.ftms.service.schedule.impl;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ScheduleResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.dto.response.SessionResponse;
import com.g96.ftms.entity.Session;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.service.schedule.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService {
    private final ModelMapper mapper;

    @Override
    public ApiResponse<List<ScheduleResponse.ScheDuleDetailsInfo>> generateTimeTable(LocalDate startDate,Integer slot, List<SessionResponse.SessionInfoDTO> sessions) {
        List<ScheduleResponse.ScheDuleDetailsInfo> list = new ArrayList<>();
        LocalDate currentDate = startDate;

        for (SessionResponse.SessionInfoDTO session : sessions) {
            // Skip weekends
            while (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                currentDate = currentDate.plusDays(1);
            }

            // Map session to ScheDuleDetailsInfo
            ScheduleResponse.ScheDuleDetailsInfo map = mapper.map(session, ScheduleResponse.ScheDuleDetailsInfo.class);

            // Convert LocalDate to LocalDateTime
            LocalDateTime dateTime = currentDate.atStartOfDay(); // Use start of the day
            // Alternatively, set a custom time: currentDate.atTime(LocalTime.of(10, 0)); // 10:00 AM

            map.setDate(dateTime); // Assuming your `ScheDuleDetailsInfo` has a LocalDateTime field `dateTime`
                dateTime.toLocalDate();
            ScheduleResponse.TimeSlotInfo timeSlot = ScheduleResponse.TimeSlotInfo.getTimeSlot(slot,dateTime.toLocalDate());
//            map.setDate(timeSlot.getStartDate());
            map.setStartDate(timeSlot.getStartDate());
            map.setEndDate(timeSlot.getEndDate());
            // Add schedule detail to the result list
            list.add(map);
            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), list);
    }

}
