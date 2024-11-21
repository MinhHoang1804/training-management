package com.g96.ftms.repository;

import com.g96.ftms.entity.MarkScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkSchemeRepository extends JpaRepository<MarkScheme, Long> {
    boolean existsByMarkSchemeIdAndSubject_SubjectId(Long markSchemeId, Long subjectId);
    List<MarkScheme> findBySubject_SubjectId(Long subjectId);
}
