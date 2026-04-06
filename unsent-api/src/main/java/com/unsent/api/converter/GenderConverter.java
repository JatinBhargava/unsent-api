package com.unsent.api.converter;

import com.unsent.util.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender gender) {
        return gender == null ? null : gender.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(String value) {
        return Gender.fromValue(value);
    }
}
