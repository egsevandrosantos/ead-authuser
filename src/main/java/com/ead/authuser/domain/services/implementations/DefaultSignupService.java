package com.ead.authuser.domain.services.implementations;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ead.authuser.domain.enumerations.UserStatus;
import com.ead.authuser.domain.enumerations.UserType;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.SignupService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultSignupService implements SignupService {
	private final UserRepository userRepository;

	@Override
	public UUID signup(User user) {
		user.setStatus(UserStatus.ACTIVE);
		user.setType(UserType.STUDENT);
		user = userRepository.save(user);
		return user.getId();
	}
	
}
