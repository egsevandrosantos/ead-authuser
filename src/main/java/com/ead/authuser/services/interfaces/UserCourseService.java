package com.ead.authuser.services.interfaces;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.services.ServiceResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Deprecated
public interface UserCourseService {
    Page<CourseDTO> findAll(Pageable pageable, UUID userId);
    ServiceResponse create(UUID userId, UserCourseDTO userCourseDTO);
    ServiceResponse deleteByCourseId(UUID courseId);
}
