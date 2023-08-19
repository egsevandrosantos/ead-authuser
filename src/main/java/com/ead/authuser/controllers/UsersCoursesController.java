package com.ead.authuser.controllers;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.services.interfaces.UserCourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users/{userId}/courses")
@Log4j2
public class UsersCoursesController {
    @Autowired
    private UserCourseService service;

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> findAll(
        @PathVariable(value = "userId") UUID userId,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(service.findAll(pageable, userId));
    }
}
