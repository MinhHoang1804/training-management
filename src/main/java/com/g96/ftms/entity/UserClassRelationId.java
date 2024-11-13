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
public class UserClassRelationId implements Serializable {
    public UserClassRelationId(Long userId, Long classId) {
        this.userId = userId;
        this.classId = classId;
    }

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "class_id")
    private Long classId;
}
