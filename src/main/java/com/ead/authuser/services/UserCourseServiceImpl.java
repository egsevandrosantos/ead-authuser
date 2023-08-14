package com.ead.authuser.services;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.interfaces.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UserCourseRepository repository;
}
