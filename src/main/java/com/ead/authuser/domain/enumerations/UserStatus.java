package com.ead.authuser.domain.enumerations;

public enum UserStatus implements BaseEnum {
	ACTIVE("A"),
	BLOCKED("B");

	private final String code;

	private UserStatus(String code) {
		this.code = code;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}
}
