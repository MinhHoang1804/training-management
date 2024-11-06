package com.g96.ftms.repository;

import com.g96.ftms.entity.CurriculumSubjectRelation;
import com.g96.ftms.entity.CurriculumSubjectRelationId;
import com.g96.ftms.entity.UserClassRelation;
import com.g96.ftms.entity.UserClassRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClassRelationRepository extends JpaRepository<UserClassRelation, UserClassRelationId> {
}
