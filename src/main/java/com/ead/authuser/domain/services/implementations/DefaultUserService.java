package com.ead.authuser.domain.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Service;

import com.ead.authuser.domain.exceptions.EntityNotFoundException;
import com.ead.authuser.domain.exceptions.UniqueConstraintException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.UserService;

@Service
public class DefaultUserService extends DefaultCrudService implements UserService {
	private final UserRepository repository;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DefaultUserService(UserRepository repository) {
		super((JpaRepositoryImplementation) repository);
		this.repository = repository;
	}

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
	public void requireNonDuplicateUniqueValues(User user) {
		requireNonDuplicatedUsername(user);
		requireNonDuplicatedEmail(user);
	}

	private void requireNonDuplicatedUsername(User user) {
		if (repository.existsByUsernameAndIdNot(user.getUsername(), user.getId())) {
			throw new UniqueConstraintException("Username already in use");
		}
	}

	private void requireNonDuplicatedEmail(User user) {
		if (repository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
			throw new UniqueConstraintException("Email already in use");
		}
	}
}
