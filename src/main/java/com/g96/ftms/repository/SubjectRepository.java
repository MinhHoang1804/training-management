package com.g96.ftms.repository;

import com.g96.ftms.dto.SubjectDTO;
import com.g96.ftms.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s JOIN s.curriculums c WHERE c.curriculumId = :curriculumId")
    List<Subject> findByCurriculumId(@Param("curriculumId") Long curriculumId);

    @Query("SELECT s FROM Subject s JOIN s.curriculums c")
    List<Subject> findAllSubjectsWithCurriculum();

}
