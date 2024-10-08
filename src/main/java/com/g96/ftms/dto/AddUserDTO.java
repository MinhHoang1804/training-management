package com.g96.ftms.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class AddUserDTO {
    private String fullName;
    private String email;
    private String imgAva;
    private String phone;
    private String address;
    private String account;
    private Date dateOfBirth;
    private Long roleId;
}
