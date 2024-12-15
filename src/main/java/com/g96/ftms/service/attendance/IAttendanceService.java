package com.g96.ftms.service.attendance;

import com.g96.ftms.dto.request.AttendanceServiceRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.AttendanceResponse;

import java.util.List;

public interface IAttendanceService {

    ApiResponse<AttendanceResponse.SearchResponse> searchByClass(AttendanceServiceRequest.SearchRequest model);

    ApiResponse<AttendanceResponse.UserAttendanceResponse> getUserAttendance(Long userId, Long classId, Long subjectId);

    ApiResponse<AttendanceResponse.UserAttendanceAllSubjectResponse> getUserTimeTable(String userName, Long classId);

    ApiResponse<?> editStatus(AttendanceServiceRequest.AttendanceUserEditRequest model);

    ApiResponse<AttendanceResponse.AttendanceReportResponse> getAttendanceReport(Long userId, Long classId, Long subjectId);

    ApiResponse<List<AttendanceResponse.AttendanceReportResponse>> getAttendanceReportByClass(AttendanceServiceRequest.SearchRequest model);
}
