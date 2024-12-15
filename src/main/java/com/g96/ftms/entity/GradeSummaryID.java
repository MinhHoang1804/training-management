package com.g96.ftms.entity;

import jakarta.persistence.Embeddable;
import lombok.*;


@Data
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GradeSummaryID {
  private Long userId;
  private Long classId;
}