package com.ead.authuser.domain.services;

import java.util.UUID;

public interface CrudService {
	void requireExistsById(UUID id);
	void deleteById(UUID id);
}
