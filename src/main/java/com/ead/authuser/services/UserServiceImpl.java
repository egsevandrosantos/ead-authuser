package com.ead.authuser.services;

import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
}
