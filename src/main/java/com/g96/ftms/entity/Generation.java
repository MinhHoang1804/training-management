package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "generation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Generation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generation_id")
    private Long getGenerationId;

    @Column(name = "generation_name")
    @NotNull
    private String generationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    @JsonBackReference
    private Settings settings;
}
