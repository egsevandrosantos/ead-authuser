package com.ead.authuser.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.ead.authuser.domain.models.User;

public interface UserRepository extends JpaRepository<User, UUID>, JpaRepositoryImplementation<User, UUID> {
	public boolean existsByUsernameAndIdNot(String username, UUID id);
	public boolean existsByEmailAndIdNot(String email, UUID id);
}
