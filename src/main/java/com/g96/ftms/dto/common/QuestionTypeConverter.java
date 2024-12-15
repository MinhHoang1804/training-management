package com.g96.ftms.dto.common;

import com.g96.ftms.util.constants.QuestionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuestionTypeConverter implements AttributeConverter<QuestionType, String> {

    @Override
    public String convertToDatabaseColumn(QuestionType attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public QuestionType convertToEntityAttribute(String dbData) {
        return dbData != null ? QuestionType.fromValue(dbData) : null;
    }
}
