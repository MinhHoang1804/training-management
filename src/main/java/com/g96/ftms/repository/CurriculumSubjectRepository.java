package com.g96.ftms.repository;

import com.g96.ftms.entity.Curriculum;
import com.g96.ftms.entity.CurriculumSubjectRelation;
import com.g96.ftms.entity.CurriculumSubjectRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumSubjectRepository extends JpaRepository<CurriculumSubjectRelation, CurriculumSubjectRelationId> {
}
