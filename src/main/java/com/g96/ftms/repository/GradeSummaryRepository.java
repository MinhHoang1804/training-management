package com.g96.ftms.repository;

import com.g96.ftms.entity.GradeSummary;
import com.g96.ftms.entity.GradeSummaryID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeSummaryRepository extends JpaRepository<GradeSummary, GradeSummaryID> {
    GradeSummary findByUser_UserIdAndClasss_ClassId(Long userId, Long classId);
}
