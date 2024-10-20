package com.g96.ftms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
public class CurriculumSubjectRelationId implements Serializable {
    @Column(name = "curriculum_id")
    private Integer curriculumId;

    @Column(name = "subject_id")
    private Integer subjectId;
}
