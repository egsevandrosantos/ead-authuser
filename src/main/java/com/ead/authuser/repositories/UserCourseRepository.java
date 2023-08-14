package com.ead.authuser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseRepository, UUID> {
}
