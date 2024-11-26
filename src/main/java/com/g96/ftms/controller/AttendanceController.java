package com.g96.ftms.controller;

import com.g96.ftms.dto.request.AttendanceServiceRequest;
import com.g96.ftms.dto.request.ClassRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.attendance.IAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attendance-management")
@RequiredArgsConstructor
public class AttendanceController {
    private final IAttendanceService attendanceService;
    @GetMapping("/search-by-class")
    public ApiResponse<?> getClassList(@RequestBody AttendanceServiceRequest.SearchRequest model) {
        return attendanceService.searchByClass(model);
    }

    @GetMapping("/attendance-by-user")
    public ApiResponse<?> getClassList(@RequestBody AttendanceServiceRequest.SearchByUserRequest model) {
        return attendanceService.getUserAttendance(model.getUserId(),model.getClassId(),model.getSubjectId());
    }

    @GetMapping("/attendance-update")
    public ApiResponse<?> attendanceEdit(@RequestBody AttendanceServiceRequest.AttendanceUserEditRequest model) {
        return attendanceService.editStatus(model);
    }
}
