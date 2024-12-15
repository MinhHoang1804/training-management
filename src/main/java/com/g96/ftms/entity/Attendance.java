package com.g96.ftms.entity;

import com.g96.ftms.util.constants.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scheduleDetailId")
    @JoinColumn(name = "schedule_detail_id")
    private ScheduleDetail scheduleDetail;


    @Column(name="status",columnDefinition = "ENUM('A','An','E','En','L','Ln','P')")
    private AttendanceStatus status;

    @LastModifiedDate
    @Column(name="record_time")
    private LocalDateTime recordTime;

    @Column(name="attendance_note")
    private String attendanceNote;
}
