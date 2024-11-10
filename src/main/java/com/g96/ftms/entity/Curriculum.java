package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "curriculums")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curriculumId;

    @Column(nullable = false)
    private String curriculumName;

    @Column(columnDefinition = "TEXT")
    private String descriptions;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    private Boolean status = true;

    @OneToMany(mappedBy = "curriculum", fetch = FetchType.LAZY)
    @JsonBackReference
    List<CurriculumSubjectRelation> curriculumSubjectRelationList;

    @OneToMany(mappedBy = "curriculum", fetch = FetchType.LAZY)
    @JsonBackReference
    List<Class> classes;
}
