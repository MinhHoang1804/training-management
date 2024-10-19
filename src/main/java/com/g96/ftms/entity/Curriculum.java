package com.g96.ftms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    // đừng sửa đoạn này không bị loop data
    @ManyToMany(mappedBy = "curriculums", fetch = FetchType.LAZY)
    private Set<Subject> subjects = new HashSet<>();

}
