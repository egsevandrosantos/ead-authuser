package com.ead.authuser.enums.converters;

import com.ead.authuser.enums.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserStatus userStatus) {
        if (userStatus == null) {
            return null;
        }
        return userStatus.value();
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }
        return UserStatus.fromValue(value);
    }
}
