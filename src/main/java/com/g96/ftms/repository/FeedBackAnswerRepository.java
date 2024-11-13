package com.g96.ftms.repository;

import com.g96.ftms.entity.FeedbackAnswer;
import com.g96.ftms.entity.FeedbackAnswerRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackAnswerRepository extends JpaRepository<FeedbackAnswer, FeedbackAnswerRelationId> {
    long deleteByFeedback_FeedbackId(Long feedbackId);
}
