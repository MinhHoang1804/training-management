package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(nullable = false)
    private String subjectCode;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String documentLink;

    @Column(nullable = false)
    private boolean status = true;

    private String descriptions;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonBackReference
    List<CurriculumSubjectRelation> curriculumSubjectRelationList;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonBackReference
    List<MarkScheme> markSchemeList;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonBackReference
    List<Schedule> schedules;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonBackReference
    List<GradeSetting> gradeSettingList;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Grade> grades;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonBackReference
    List<FeedBack> feedBackList;

}
