package com.g96.ftms.controller;

import com.g96.ftms.dto.request.AttendanceServiceRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.service.attendance.IAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendance-management")
@RequiredArgsConstructor
public class AttendanceController {
    private final IAttendanceService attendanceService;
    @PostMapping("/search-by-class")
    public ApiResponse<?> getClassList(@RequestBody AttendanceServiceRequest.SearchRequest model) {
        return attendanceService.searchByClass(model);
    }

    @PostMapping("/attendance-by-user")
    public ApiResponse<?> getClassList(@RequestBody AttendanceServiceRequest.SearchByUserRequest model) {
        return attendanceService.getUserTimeTable(model.getUserName(),model.getClassId());
    }

    @PostMapping("/attendance-update")
    public ApiResponse<?> attendanceEdit(@RequestBody AttendanceServiceRequest.AttendanceUserEditRequest model) {
        return attendanceService.editStatus(model);
    }

    @PostMapping("/attendance-report-user")
    public ApiResponse<?> getAttendanceReportUser(@RequestBody AttendanceServiceRequest.AttendanceUserReportRequest model) {
        return attendanceService.getAttendanceReport(model.getUserId(), model.getClassId(),model.getSubjectId());
    }
    @PostMapping("/attendance-report-list")
    public ApiResponse<?> getAttendanceReportUser(@RequestBody AttendanceServiceRequest.SearchRequest model) {
        return attendanceService.getAttendanceReportByClass(model);
    }
}
