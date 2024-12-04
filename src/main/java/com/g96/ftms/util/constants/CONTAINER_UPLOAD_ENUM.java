package com.g96.ftms.util.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CONTAINER_UPLOAD_ENUM {
    AVATAR("avatar-image"),
    IMAGE("antifact-image-container");
    private final String value;

    CONTAINER_UPLOAD_ENUM(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
