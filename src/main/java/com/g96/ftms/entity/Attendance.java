package com.g96.ftms.entity;

import com.g96.ftms.util.constants.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Attendance {
    @EmbeddedId
    AttendanceId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("scheduleDetailId")
    @JoinColumn(name = "schedule_detail_id")
    private ScheduleDetail scheduleDetail;


    @Column(name="status",columnDefinition = "ENUM('A','An','E','En','L','Ln','P')")
    private AttendanceStatus status;

    @Column(name="record_time")
    private LocalDateTime recordTime;

    @Column(name="attendance_note")
    private String attendanceNote;
}
