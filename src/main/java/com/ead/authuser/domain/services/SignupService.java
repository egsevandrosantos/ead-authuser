package com.ead.authuser.domain.services;

import java.util.UUID;

import com.ead.authuser.domain.models.User;

public interface SignupService {
	public UUID signup(User user);
}
