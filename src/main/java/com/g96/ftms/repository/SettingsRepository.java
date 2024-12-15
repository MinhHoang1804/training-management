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
    boolean existsByDescriptionAndLocation_LocationNameAndSettingIdNot(String description, String locationName, Long settingId);
    boolean existsByDescriptionAndLocation_LocationName(String description, String locationName);
    boolean existsByDescriptionAndGeneration_GenerationName(String description, String generationName);
    @Query("SELECT s FROM Settings s " +
            "LEFT JOIN s.location r " +
            "LEFT JOIN s.generation g " +
            "WHERE (:keyword IS NULL OR s.description LIKE %:keyword% " +
            "OR r.locationName LIKE %:keyword% " +
            "OR g.generationName LIKE %:keyword%) " +
            "AND (:status IS NULL OR s.status = :status)")
    Page<Settings> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("status") Boolean status,
            Pageable pageable
    );

    Settings findByDescription(String description);

    boolean existsByGeneration_GenerationName(String generationName);

    boolean existsByLocation_LocationName(String locationName);

    boolean existsByLocation_LocationNameAndSettingIdNot(String locationName, Long settingId);

    boolean existsByGeneration_GenerationNameAndSettingIdNot(String generationName, Long settingId);
}
