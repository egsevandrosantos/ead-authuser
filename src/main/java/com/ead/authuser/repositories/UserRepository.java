package com.ead.authuser.repositories;

import com.ead.authuser.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdIsNot(String username, UUID id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdIsNot(String email, UUID id);
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteById(UUID id); // Evitar a consulta SELECT antes do DELETE
}
