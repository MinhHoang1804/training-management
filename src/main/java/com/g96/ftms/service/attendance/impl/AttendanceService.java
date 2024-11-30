package com.g96.ftms.service.attendance.impl;

import com.g96.ftms.dto.request.AttendanceServiceRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.AttendanceResponse;
import com.g96.ftms.dto.response.TraineeResponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.attendance.IAttendanceService;
import com.g96.ftms.util.constants.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceService implements IAttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;

    @Override
    public ApiResponse<AttendanceResponse.SearchResponse> searchByClass(AttendanceServiceRequest.SearchRequest model) {
        List<User> list = userRepository.findUsersByClassId(model.getClassId());
        List<AttendanceResponse.UserAttendanceResponse> listAttendances = new ArrayList<>();
        for (User user : list) {
            AttendanceResponse.UserAttendanceResponse data = getUserAttendance(user.getUserId(), model.getClassId(), model.getSubjectId()).getData();
            if (data != null) {
                listAttendances.add(data);
            }
        }
        AttendanceResponse.SearchResponse response = AttendanceResponse.SearchResponse.builder().listAttendances(listAttendances).build();
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
            if (attendance != null) {
                AttendanceResponse.AttendanceStatusResponse attendanceStatus = AttendanceResponse.AttendanceStatusResponse.builder()
                        .status(attendance.getStatus())
                        .attendanceNote(attendance.getAttendanceNote())
                        .scheduleDetailId(scheduleDetail.getScheduleDetailId())
                        .startDate(attendance.getScheduleDetail().getStartTime())
                        .endDate(attendance.getScheduleDetail().getEndTime())
                        .build();
                litAttendanceStatuses.add(attendanceStatus);

            }
        }
        AttendanceResponse.UserAttendanceResponse response = AttendanceResponse.UserAttendanceResponse.builder()
                .userId(userId)
                .fullName(user.getFullName())
                .litAttendanceStatuses(litAttendanceStatuses)
                .build();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<AttendanceResponse.UserAttendanceAllSubjectResponse> getUserTimeTable(String userName, Long classId) {
        User user = userRepository.findByAccount(userName);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND);
        }
       List<AttendanceResponse.UserAttendanceSubject> listSubjectTimeTable = new ArrayList<>();
        //get subject in class
        Class c = classRepository.findById(classId).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, ErrorCode.CLASS_NOT_FOUND));
        List<Subject> list = c.getCurriculum().getCurriculumSubjectRelationList().stream().map(CurriculumSubjectRelation::getSubject).toList();
        for (Subject subject : list) {
            AttendanceResponse.UserAttendanceResponse data = getUserAttendance(user.getUserId(), classId, subject.getSubjectId()).getData();
            AttendanceResponse.UserAttendanceSubject builder=AttendanceResponse.UserAttendanceSubject.builder()
                    .subjectName(subject.getSubjectName())
                    .listWeeklyAttendances(data.getLitAttendanceStatuses())
                    .build();
            listSubjectTimeTable.add(builder);
        }
        AttendanceResponse.UserAttendanceAllSubjectResponse response = AttendanceResponse.UserAttendanceAllSubjectResponse.builder()
                .listSubjectTimeTable(listSubjectTimeTable)
                .userId(user.getUserId())
                .userName(user.getAccount())
                .fullName(user.getFullName())
                .className(c.getClassCode())
                .build();
        return new ApiResponse<AttendanceResponse.UserAttendanceAllSubjectResponse>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<?> editStatus(AttendanceServiceRequest.AttendanceUserEditRequest model) {
        List<Attendance> attendanceList = new ArrayList<>();
        for (AttendanceServiceRequest.AttendanceUserStatus a : model.getData()) {
            Attendance attendance = attendanceRepository.findByUser_UserIdAndScheduleDetail_ScheduleDetailId(a.getUserId(), a.getUserId());
            if (attendance != null) {
                attendance.setStatus(a.getStatus());
                attendance.setAttendanceNote(a.getAttendanceNote());
                attendanceList.add(attendance);
            }
        }
        attendanceRepository.saveAll(attendanceList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");

    }

}
