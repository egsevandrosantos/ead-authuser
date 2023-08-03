package com.ead.authuser.repositories;

import com.ead.authuser.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdIsNot(String username, UUID id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdIsNot(String email, UUID id);
}
