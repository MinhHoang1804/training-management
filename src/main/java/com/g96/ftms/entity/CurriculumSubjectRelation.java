package com.g96.ftms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curriculum_subject")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumSubjectRelation {
    @EmbeddedId
    CurriculumSubjectRelationId id;
    @ManyToOne
    @MapsId("curriculumId")
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    @ManyToOne
    @MapsId("subjectId")
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "weight_percentage")
    private Double weightPercentage;
}
