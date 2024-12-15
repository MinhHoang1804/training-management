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
public class AttendanceId implements Serializable {
    @Column(name = "userId")
    private Long userId;

    @Column(name = "schedule_detail_id")
    private Long scheduleDetailId;

}
