package com.ead.authuser.services.interfaces;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserCourseService {
    Page<CourseDTO> findAll(Pageable pageable, UUID userId);
    UUID create(UserCourseDTO userCourseDTO);
    boolean valid(UserCourseDTO userCourseDTO);
    void merge(UserCourseDTO source, UserCourseDTO dest);
    void merge(UserCourseDTO source, UserCourseDTO dest, Class<? extends UserCourseDTO.UserCourseView> view);
}
