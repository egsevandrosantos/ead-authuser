package com.ead.authuser.domain.services;

import java.util.List;
import java.util.UUID;

import com.ead.authuser.domain.models.User;

public interface UserService extends CrudService {
	List<User> findAll();
	User findById(UUID id);
	void requireNonDuplicateUniqueValues(User user);
}
