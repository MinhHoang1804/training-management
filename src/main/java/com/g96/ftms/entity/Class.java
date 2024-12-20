package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "class")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_code")
    private String classCode;

    @Column
    private Boolean status;

    private String descriptions;

    @Column
    private String admin;

    @Column
    private String supplier;

    @Column(updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column
    private LocalDateTime endDate;

    @Column
    private LocalDateTime startDate;


    @Column(name="plan_Trainee_no")
    private Integer planTraineeNo;

    @Column(name="manager")
    private String manager;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "classs",fetch = FetchType.LAZY)
    @JsonBackReference
    List<Schedule> schedules;

    @OneToMany(mappedBy = "classs",fetch = FetchType.LAZY)
    @JsonBackReference
    List<UserClassRelation> userClassRelationList;


    @OneToMany(mappedBy = "classs", fetch = FetchType.LAZY)
    private List<Grade> grades;

    @OneToMany(mappedBy = "classs",fetch = FetchType.LAZY)
    @JsonBackReference
    List<FeedBack> feedBackList;

    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    @ManyToOne
    @JoinColumn(name = "generation_id")
    private Generation generation;

    @OneToMany(mappedBy = "classs", fetch = FetchType.LAZY)
    private List<GradeSummary> gradesSummaryList;
}

