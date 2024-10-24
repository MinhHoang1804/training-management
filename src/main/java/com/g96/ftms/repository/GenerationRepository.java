package com.g96.ftms.repository;

import com.g96.ftms.entity.Curriculum;
import com.g96.ftms.entity.Generation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenerationRepository  extends JpaRepository<Generation, Long> {
    boolean existsByGenerationName(String generationName);
}
