package com.g96.ftms.dto.common;

import com.g96.ftms.util.constants.AttendanceStatus;
import com.g96.ftms.util.constants.QuestionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AttendanceTypeConverter implements AttributeConverter<AttendanceStatus, String> {

    @Override
    public String convertToDatabaseColumn(AttendanceStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public AttendanceStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? AttendanceStatus.fromValue(dbData) : null;
    }
}
