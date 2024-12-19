package com.ead.authuser.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ead.authuser.domain.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
