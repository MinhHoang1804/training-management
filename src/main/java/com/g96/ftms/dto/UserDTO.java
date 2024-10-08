package com.g96.ftms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String fullName;
    private String email;
    private String imgAva;
    private String phone;
    private String emergencyPhone;
    private Date dateOfBirth;
    private Date createdDate;
    private String address;
    private String account;
    private Set<String> roles;
  
}
