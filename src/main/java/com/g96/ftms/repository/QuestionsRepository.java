package com.g96.ftms.repository;

import com.g96.ftms.entity.MarkScheme;
import com.g96.ftms.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {
}
