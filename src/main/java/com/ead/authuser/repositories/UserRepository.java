package com.ead.authuser.repositories;

import com.ead.authuser.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsernameAndIdIsNot(String username, UUID id);
    boolean existsByEmailAndIdIsNot(String email, UUID id);
}
