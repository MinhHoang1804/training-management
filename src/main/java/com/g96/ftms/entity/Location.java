package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "location_name")
    private String locationName;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Settings> settings = new ArrayList<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Class> classList = new ArrayList<>();
}
