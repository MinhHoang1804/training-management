package com.g96.ftms.repository;

import com.g96.ftms.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository  extends JpaRepository<Attendance, Long> {
    Attendance findByUser_UserIdAndScheduleDetail_ScheduleDetailId(Long userId, Long scheduleDetailId);
}
