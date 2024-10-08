package com.g96.ftms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;


import java.sql.Date;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name", nullable = false)
    @Size(max = 255)
    private String fullName;

    @Column(name = "email", unique = true)
    @Size(max = 320)
    private String email;

    @Column(name = "img_ava")
    @Size(max = 600)
    private String imgAva = "default_avatar.png";

    @Column(name = "phone", nullable = false, unique = true)
    @Size(max = 15)
    private String phone;

    @Column(name = "emergency_phone")
    @Size(max = 15)
    private String emergencyPhone;

    @Column(name = "address")
    @Size(max = 100)
    private String address;

    @Column(name = "account", nullable = false, unique = true)
    @Size(max = 50)
    private String account;

    @Column(name = "password", nullable = false)
    @Size(max = 255)
    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "created_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createdDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )


    private Set<Role> roles;


    @JsonProperty("role")
    public String getRole() {
        return roles.stream()
                .findFirst()
                .map(Role::getRoleName)
                .orElse(null);
    }
    @JsonProperty("roleNames")
    public String getRoleNames() {
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet()).toString();
    }





}
