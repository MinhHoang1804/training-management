package com.g96.ftms.repository;

import com.g96.ftms.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    long deleteBySubject_SubjectIdAndClasss_ClassId(Long subjectId, Long classId);
    List<Schedule> findByClasss_ClassIdAndSubject_SubjectId(Long classId, Long subjectId);
    @Query("SELECT DISTINCT s.trainer FROM Schedule s WHERE " +
            "(s.startDate <= :endDate AND s.endDate >= :startDate)")
    List<String> findTraineesInRangeAndSlot(
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    Schedule findBySubject_SubjectIdAndClasss_ClassId(Long subjectId, Long classId);
}
