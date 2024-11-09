package com.g96.ftms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    Long feedbackId;

    @Column
    private Double avgRating;

    @Column
    private LocalDateTime feedBackDate = LocalDateTime.now();

    @Column
    private LocalDateTime openTime;

    @Column
    private LocalDateTime lastUpdateTime;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classs;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}