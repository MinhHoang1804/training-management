package com.g96.ftms.util.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_TRAINER("ROLE_TRAINER"),
    ROLE_TRAINEE("ROLE_TRAINEE"),
    ROLE_CLASS_ADMIN("ROLE_CLASS_ADMIN");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}