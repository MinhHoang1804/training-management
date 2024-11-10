package com.g96.ftms.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GradeId implements Serializable {
    private Long userId;
    private Long classId;
    private Long subjectId;
    private Long markSchemeId;
}