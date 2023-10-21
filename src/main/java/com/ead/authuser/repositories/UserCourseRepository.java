package com.ead.authuser.repositories;

import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.List;

@Deprecated
public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {
    boolean existsByUserAndCourseId(User user, UUID courseId);
    boolean existsByCourseId(UUID courseId);
    List<UserCourse> findByUser(User user);
    List<UserCourse> findAllByCourseId(UUID courseId);
    @Modifying
    @Query("DELETE FROM UserCourse uc WHERE uc.courseId = :courseId")
    void deleteAllByCourseId(UUID courseId); // Evitar a consulta SELECT antes do DELETE
    @Modifying
    @Query("DELETE FROM UserCourse uc WHERE uc.user.id = :userId")
    void deleteAllByUser(UUID userId); // Evitar a consulta SELECT antes do DELETE
}
