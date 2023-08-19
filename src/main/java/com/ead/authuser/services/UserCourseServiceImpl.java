package com.ead.authuser.services;

import com.ead.authuser.clients.UserCourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.interfaces.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UserCourseRepository repository;
    @Autowired
    private UserCourseClient client;

    @Override
    public Page<CourseDTO> findAll(Pageable pageable, UUID userId) {
        return client.findAll(pageable, userId);
    }
}
