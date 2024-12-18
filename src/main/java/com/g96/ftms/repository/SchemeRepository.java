package com.g96.ftms.repository;

import com.g96.ftms.entity.MarkScheme;
import com.g96.ftms.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemeRepository  extends JpaRepository<MarkScheme, Long> {
    long deleteBySubject_SubjectId(Long subjectId);
    @Modifying
    @Query("DELETE FROM MarkScheme s WHERE s.markSchemeId NOT IN :ids")
    void removeRangeExclude(List<Long> ids);

    @Modifying
    @Query("UPDATE MarkScheme ms SET ms.grades = null, ms.subject = null WHERE ms.markSchemeId IN :ids")
    void clearGradesAndSubject(List<Long> ids);
}
