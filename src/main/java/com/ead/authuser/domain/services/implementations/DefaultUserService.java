package com.ead.authuser.domain.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ead.authuser.domain.exceptions.EntityNotFoundException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {
	private final UserRepository repository;

	@Override
	public List<User> findAll() {
		return repository.findAll();
	}
	
	@Override
	public User findById(UUID id) {
		return repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException());
	}
	
	@Override
	public void deleteById(UUID id) {
		this.requireNonEmptyById(id);
		repository.deleteById(id);
	}
	
	private void requireNonEmptyById(UUID id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException();
		}
	}
}
