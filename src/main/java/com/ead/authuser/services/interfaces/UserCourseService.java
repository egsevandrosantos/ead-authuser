package com.ead.authuser.services.interfaces;

import com.ead.authuser.dtos.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserCourseService {
    Page<CourseDTO> findAll(Pageable pageable, UUID userId);
}
