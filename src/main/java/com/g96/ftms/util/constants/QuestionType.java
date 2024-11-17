package com.g96.ftms.util.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum QuestionType {
    RATING("rating"),
    YES_NO("yes_no"),
    TEXT("text");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static QuestionType fromValue(String value) {
        for (QuestionType type : QuestionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + value);
    }
}
