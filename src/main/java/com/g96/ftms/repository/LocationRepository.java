package com.g96.ftms.repository;

import com.g96.ftms.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLocationNameAndLocationIdNot(String locationName, Long locationId);
    boolean existsByLocationNameAndLocationId(String locationName, Long locationId);

    Optional<Location> findByLocationName(String locationName);

    List<Location> findBySettings_Status(Boolean status);
}
