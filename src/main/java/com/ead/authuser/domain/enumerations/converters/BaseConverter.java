package com.ead.authuser.domain.enumerations.converters;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.ead.authuser.domain.enumerations.BaseEnum;

import jakarta.persistence.AttributeConverter;

public abstract class BaseConverter<X extends BaseEnum, Y extends CharSequence> implements AttributeConverter<X, Y> {

	@SuppressWarnings("unchecked")
	@Override
	public Y convertToDatabaseColumn(X attribute) {
		return (Y) Optional.ofNullable(attribute)
			.map(BaseEnum::getCode)
			.orElse(null);
	}

	public BaseEnum convertToEntityAttribute(BaseEnum[] values, Y dbData) {
		return Optional.ofNullable(dbData)
			.map(
				code -> Stream.of(values)
					.filter(e -> Objects.equals(e.getCode(), code))
					.findFirst()
					.orElseThrow(IllegalArgumentException::new)
			)
			.orElse(null);
	}

}
