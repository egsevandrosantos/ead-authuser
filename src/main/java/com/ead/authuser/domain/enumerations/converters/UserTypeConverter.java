package com.ead.authuser.domain.enumerations.converters;

import com.ead.authuser.domain.enumerations.UserType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter extends BaseConverter<UserType, String> {

	@Override
	public UserType convertToEntityAttribute(String dbData) {
		return (UserType) super.convertToEntityAttribute(UserType.values(), dbData);
	}

}
