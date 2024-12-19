package com.ead.authuser.domain.enumerations;

public enum UserType implements BaseEnum {
	ADMIN("A"),
	STUDENT("S"),
	INSTRUCTOR("I");

	private final String code;
	
	private UserType(String code) {
		this.code = code;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}

}
