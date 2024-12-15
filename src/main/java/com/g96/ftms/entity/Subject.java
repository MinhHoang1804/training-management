package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
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

    @Column
    private String subjectCode;

    @Column
    private String subjectName;

    @Column
    private String documentLink;

    @Column
    private boolean status = true;

    private String descriptions;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column
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


    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Session> sessionsList;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonBackReference
    List<FeedBack> feedBackList;

}
