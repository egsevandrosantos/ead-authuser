package com.ead.authuser.domain.enumerations.converters;

import com.ead.authuser.domain.enumerations.UserStatus;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter extends BaseConverter<UserStatus, String> {

	@Override
	public UserStatus convertToEntityAttribute(String dbData) {
		return (UserStatus) super.convertToEntityAttribute(UserStatus.values(), dbData);
	}

}
