package com.g96.ftms.service.attendance.impl;

import com.g96.ftms.dto.request.AttendanceServiceRequest;
import com.g96.ftms.dto.response.ApiResponse;
import com.g96.ftms.dto.response.AttendanceResponse;
import com.g96.ftms.entity.*;
import com.g96.ftms.entity.Class;
import com.g96.ftms.exception.AppException;
import com.g96.ftms.exception.ErrorCode;
import com.g96.ftms.repository.*;
import com.g96.ftms.service.attendance.IAttendanceService;
import com.g96.ftms.util.constants.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        if (schedule == null) {
            throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.SCHEDULE_NOT_FOUND);
        }
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
                        .trainer(scheduleDetail.getSchedule().getTrainer())
                        .location(scheduleDetail.getSchedule().getLocation().getLocationName())
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
            AttendanceResponse.UserAttendanceSubject builder = AttendanceResponse.UserAttendanceSubject.builder()
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
            Attendance attendance = attendanceRepository.findByUser_UserIdAndScheduleDetail_ScheduleDetailId(a.getUserId(), a.getScheduleDetailId());
            if (attendance != null) {
                attendance.setStatus(a.getStatus());
                attendance.setAttendanceNote(a.getAttendanceNote());
                attendanceList.add(attendance);
            }
        }
        attendanceRepository.saveAll(attendanceList);
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), "Success");

    }

    @Override
    public ApiResponse<AttendanceResponse.AttendanceReportResponse> getAttendanceReport(Long userId, Long classId, Long subjectId) {
        ApiResponse<AttendanceResponse.UserAttendanceResponse> userAttendance = getUserAttendance(userId, classId, subjectId);
        if (userAttendance == null) throw new AppException(HttpStatus.NOT_FOUND, ErrorCode.SCHEDULE_NOT_FOUND);
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        List<AttendanceStatus> list = userAttendance.getData().getLitAttendanceStatuses().stream().map(AttendanceResponse.AttendanceStatusResponse::getStatus).collect(Collectors.toList());
        AttendanceResponse.AttendanceReportStatusDTO report = generateAttendanceReport(list);
        AttendanceResponse.AttendanceReportResponse response = AttendanceResponse.AttendanceReportResponse.builder()
                .report(report)
                .fullName(user.getFullName())
                .userName(user.getAccount())
                .build();
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    @Override
    public ApiResponse<List<AttendanceResponse.AttendanceReportResponse>> getAttendanceReportByClass(AttendanceServiceRequest.SearchRequest model) {
        List<User> userList = userRepository.findUsersByClassId(model.getClassId());
        List<AttendanceResponse.AttendanceReportResponse> response = new ArrayList<>();
        for (User user : userList) {
            AttendanceResponse.AttendanceReportResponse data = getAttendanceReport(user.getUserId(), model.getClassId(), model.getSubjectId()).getData();
            if (data != null) {
                response.add(data);
            }
        }
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), response);
    }

    public AttendanceResponse.AttendanceReportStatusDTO generateAttendanceReport(List<AttendanceStatus> list) {
        int total = list.size();
        int absent = calculateAbsent(list); //1
        int late = calculateLate(list); //2
        double noPermissionRate = calculateNoPermissionRate(list);//3
        double sample = ((late * 1.0 / 2 + absent) / total); //4
        double disciplinePoints = calculateDisciplinePoints(sample, noPermissionRate); //5
        AttendanceResponse.AttendanceReportStatusDTO response = AttendanceResponse.AttendanceReportStatusDTO.builder()
                .absent(absent).late(late).noPermissionRate(noPermissionRate).disciplinePoints(disciplinePoints)
                .build();
        return response;
    }

    public int calculateAbsent(List<AttendanceStatus> statuses) {
        int absentCount = 0;
        for (AttendanceStatus status : statuses) {
            if (status == AttendanceStatus.A || status == AttendanceStatus.An) {
                absentCount++;
            }
        }
        return absentCount;
    }

    public int calculateLate(List<AttendanceStatus> statuses) {
        int lateCount = 0;
        for (AttendanceStatus status : statuses) {
            if (status == AttendanceStatus.L || status == AttendanceStatus.Ln || status == AttendanceStatus.En) {
                lateCount++;
            }
        }
        return lateCount;
    }

    public double calculateNoPermissionRate(List<AttendanceStatus> statuses) {
        int absentCount = calculateAbsent(statuses);
        int lateCount = calculateLate(statuses);

        int noPermissionCount = 0;
        for (AttendanceStatus status : statuses) {
            if (status == AttendanceStatus.An || status == AttendanceStatus.Ln || status == AttendanceStatus.En) {
                noPermissionCount++;
            }
        }

        int totalAttendance = absentCount + lateCount;
        return totalAttendance > 0
                ? (double) noPermissionCount * 100 / totalAttendance
                : 0.0;
    }

    public double calculateDisciplinePoints(double sample, double noPermissionRate) {
        if (sample <= 0.05) {
            return 100.0; // 100% nếu sample <= 5%
        } else if (sample <= 0.20) {
            return 80.0; // 80% nếu sample <= 20%
        } else if (sample <= 0.30) {
            return 60.0; // 60% nếu sample <= 30%
        } else if (sample < 0.50) {
            return 50.0; // 50% nếu sample < 50%
        } else if (sample >= 0.50 && noPermissionRate < 20.0) {
            return 20.0; // 20% nếu sample >= 50% và noPermissionRate < 20%
        } else if (sample >= 0.50 && noPermissionRate >= 20.0) {
            return 0.0; // 0% nếu sample >= 50% và noPermissionRate >= 20%
        } else {
            return 0.0;
        }
//        throw new IllegalArgumentException("Invalid input values for sample or noPermissionRate");
    }
}
