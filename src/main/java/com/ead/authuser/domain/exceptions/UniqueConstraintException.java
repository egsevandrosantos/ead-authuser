package com.ead.authuser.domain.exceptions;

public class UniqueConstraintException extends RuntimeException {

	private static final long serialVersionUID = -81010237423833322L;

	public UniqueConstraintException(String message) {
		super(message);
	}
}
