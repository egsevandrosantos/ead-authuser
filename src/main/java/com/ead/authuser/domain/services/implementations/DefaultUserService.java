package com.ead.authuser.domain.services.implementations;

import org.springframework.stereotype.Service;

import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {
	private final UserRepository repository;
}
