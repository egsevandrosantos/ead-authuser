package com.ead.authuser.domain.models.listeners;

import org.springframework.context.annotation.Lazy;

import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.services.UserService;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UserListener {
	private final UserService service;

	public UserListener(@Lazy UserService service) {
		this.service = service;
	}
	
	@PrePersist
	@PreUpdate
	public void beforeSave(User user) {
		service.requireNonDuplicateUniqueValues(user);
	}
}
