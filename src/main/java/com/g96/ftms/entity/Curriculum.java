package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "curriculums")
@Data
@EntityListeners(AuditingEntityListener.class)
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

    @Column
    @LastModifiedDate
    private LocalDateTime updatedDate;

    private Boolean status = true;

    @OneToMany(mappedBy = "curriculum", fetch = FetchType.LAZY)
    @JsonBackReference
    List<CurriculumSubjectRelation> curriculumSubjectRelationList;

    @OneToMany(mappedBy = "curriculum", fetch = FetchType.LAZY)
    @JsonBackReference
    List<Class> classes;
}
