package com.g96.ftms.util.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AttendanceStatus {
    A("a"),
    An("yes_no"),
    En("text"),
    L("l"),
    Ln("Ln"),
    P("p");


    private final String value;

    AttendanceStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AttendanceStatus fromValue(String value) {
        for (AttendanceStatus type : AttendanceStatus.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + value);
    }
}
