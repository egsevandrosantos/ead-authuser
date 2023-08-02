package com.ead.authuser.enums.converters;

import com.ead.authuser.enums.UserType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserTypeConverter implements AttributeConverter<UserType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserType userType) {
        if (userType == null) {
            return null;
        }
        return userType.value();
    }

    @Override
    public UserType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }
        return UserType.fromValue(value);
    }
}
