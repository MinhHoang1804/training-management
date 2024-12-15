package com.g96.ftms.dto.request;

import com.g96.ftms.dto.common.PagingBaseParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

public class UserRequest {
    @Data
    @NoArgsConstructor
    public static class UserPagingRequest extends PagingBaseParams {
        private String keyword;
        private Boolean status;
        private Long roleId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserEditProfileRequest{
        private String phone;
        private String emergencyPhone;
        private String address;
        private Date dateOfBirth;
    }
}
