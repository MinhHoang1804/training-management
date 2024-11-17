package com.g96.ftms.service.schedule.impl;

import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.ScheduleResponse;
import com.g96.ftms.dto.response.SchemeResponse;
import com.g96.ftms.entity.Session;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.service.schedule.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService {
    private final ModelMapper mapper;

    @Override
    public ApiResponse<List<ScheduleResponse.ScheDuleDetailsInfo>> generateTimeTable(LocalDate startDate, List<Session> sessions) {
        List<ScheduleResponse.ScheDuleDetailsInfo> list=new ArrayList<>();
        LocalDate currentDate = startDate;
        for (Session session : sessions) {
            // Bỏ qua ngày cuối tuần
            while (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                currentDate = currentDate.plusDays(1);
            }
            ScheduleResponse.ScheDuleDetailsInfo map = mapper.map(session, ScheduleResponse.ScheDuleDetailsInfo.class);
            map.setDate(currentDate);
            // Thêm schedule detail vào danh sách kết quả
            list.add(map);
            // Chuyển sang ngày tiếp theo
            currentDate = currentDate.plusDays(1);
        }

        return new ApiResponse<List<ScheduleResponse.ScheDuleDetailsInfo>>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), list);
    }
}
