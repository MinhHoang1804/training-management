package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long settingId;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "settings", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "settings", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Generation> generations = new ArrayList<>();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
