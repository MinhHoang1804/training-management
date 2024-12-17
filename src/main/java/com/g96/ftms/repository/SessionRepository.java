package com.g96.ftms.repository;

import com.g96.ftms.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Modifying
    @Query("DELETE FROM Session s WHERE s.sessionId NOT IN :ids")
    void removeRangeExclude(@Param("ids") List<Long> ids);
}
