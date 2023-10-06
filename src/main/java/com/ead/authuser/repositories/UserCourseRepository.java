package com.ead.authuser.repositories;

import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;


public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {
    boolean existsByUserAndCourseId(User user, UUID courseId);
    List<UserCourse> findByUser(User user);
    List<UserCourse> findAllByCourseId(UUID courseId);
    void deleteByCourseId(UUID courseId);
}
