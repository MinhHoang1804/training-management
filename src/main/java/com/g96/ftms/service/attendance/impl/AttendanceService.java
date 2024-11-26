package com.g96.ftms.service.attendance.impl;

import com.g96.ftms.dto.request.AttendanceServiceRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.AttendanceResponse;
import com.g96.ftms.dto.response.TraineeResponse;
import com.g96.ftms.entity.Attendance;
import com.g96.ftms.entity.Schedule;
import com.g96.ftms.entity.ScheduleDetail;
import com.g96.ftms.entity.User;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.AttendanceRepository;
import com.g96.ftms.repository.ScheduleDetailRepository;
import com.g96.ftms.repository.ScheduleRepository;
import com.g96.ftms.repository.UserRepository;
import com.g96.ftms.service.attendance.IAttendanceService;
import com.g96.ftms.util.constants.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService implements IAttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<AttendanceResponse.SearchResponse> searchByClass(AttendanceServiceRequest.SearchRequest model) {
        List<User> list = userRepository.findUsersByClassId(model.getClassId());
        List<AttendanceResponse.UserAttendanceResponse>listAttendances =new ArrayList<>();
        for (User user : list) {
            AttendanceResponse.UserAttendanceResponse data = getUserAttendance(user.getUserId(), model.getClassId(), model.getSubjectId()).getData();
            if (data != null) {
                listAttendances.add(data);
            }
        }
        AttendanceResponse.SearchResponse response= AttendanceResponse.SearchResponse.builder().listAttendances(listAttendances).build();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<AttendanceResponse.UserAttendanceResponse> getUserAttendance(Long userId, Long classId, Long subjectId) {
        Schedule schedule = scheduleRepository.findBySubject_SubjectIdAndClasss_ClassId(subjectId, classId);
        List<ScheduleDetail> scheduleDetailList = schedule.getScheduleDetailList();
        List<AttendanceResponse.AttendanceStatusResponse> litAttendanceStatuses = new ArrayList<>();

        User user = userRepository.findById(userId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        for (ScheduleDetail scheduleDetail : scheduleDetailList) {
            Attendance attendance = attendanceRepository.findByUser_UserIdAndScheduleDetail_ScheduleDetailId(userId, scheduleDetail.getScheduleDetailId());
            AttendanceResponse.AttendanceStatusResponse attendanceStatus = AttendanceResponse.AttendanceStatusResponse.builder()
                    .status(attendance.getStatus())
                    .attendanceNote(attendance.getAttendanceNote())
                    .scheduleDetailId(scheduleDetail.getScheduleDetailId())
                    .build();
            litAttendanceStatuses.add(attendanceStatus);
        }
        AttendanceResponse.UserAttendanceResponse response = AttendanceResponse.UserAttendanceResponse.builder()
                .userId(userId)
                .fullName(user.getFullName())
                .litAttendanceStatuses(litAttendanceStatuses)
                .build();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<?> editStatus(AttendanceServiceRequest.AttendanceUserEditRequest model) {
        List<Attendance> attendanceList=new ArrayList<>();
        for(AttendanceServiceRequest.AttendanceUserStatus a : model.getData()){
            Attendance attendance = attendanceRepository.findByUser_UserIdAndScheduleDetail_ScheduleDetailId(a.getUserId(), a.getUserId());
            if(attendance!=null){
                attendance.setStatus(a.getStatus());
                attendance.setAttendanceNote(a.getAttendanceNote());
                attendanceList.add(attendance);
            }
        }
        attendanceRepository.saveAll(attendanceList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");

    }

}
