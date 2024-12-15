package com.g96.ftms.repository;

import com.g96.ftms.entity.FeedbackAnswer;
import com.g96.ftms.entity.FeedbackAnswerRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackAnswerRepository extends JpaRepository<FeedbackAnswer, FeedbackAnswerRelationId> {
    long deleteByFeedback_FeedbackId(Long feedbackId);

    @Query("SELECT AVG(fa.rating) FROM FeedbackAnswer fa WHERE fa.id.feedbackId = :feedbackId")
    Double findAverageRatingByFeedbackId(@Param("feedbackId") Long feedbackId);
}
