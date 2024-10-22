package com.g96.ftms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CurriculumSubjectRelationId implements Serializable {
    @Column(name = "curriculum_id")
    private Long curriculumId;

    @Column(name = "subject_id")
    private Long subjectId;
}
