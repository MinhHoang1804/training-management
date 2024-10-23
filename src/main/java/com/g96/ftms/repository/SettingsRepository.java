package com.g96.ftms.repository;

import com.g96.ftms.entity.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    @Query("SELECT s FROM Settings s " +
            "LEFT JOIN s.rooms r " +
            "LEFT JOIN s.generations g " +
            "WHERE (:keyword IS NULL OR s.description LIKE %:keyword% " +
            "OR r.roomName LIKE %:keyword% " +
            "OR g.generationName LIKE %:keyword%) " +
            "AND (:status IS NULL OR s.status = :status)")
    Page<Settings> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("status") Boolean status,
            Pageable pageable
    );
}
