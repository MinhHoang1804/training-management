package com.g96.ftms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumDTO {
    private Long curriculumId;
    private String curriculumName;
    private String descriptions;
    private String createdDate;
    private Boolean status;
    private List<SubjectDTO> subjects;
}
