package com.ead.authuser.controllers;

import java.util.Base64;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.services.ServiceResponse;
import com.ead.authuser.services.interfaces.UserCourseService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/courses/{courseId}/users")
@Deprecated
public class CoursesUsersController {
    @Value("${ead.api.url.course}")
    private String coursesURI;

    @Autowired
    private UserCourseService service;

    @DeleteMapping
    public ResponseEntity<?> delete(
        @PathVariable(value = "courseId") UUID courseId,
        HttpServletRequest request
    ) {
        String requestedBy = request.getHeader("Requested-By");
        if (requestedBy == null || requestedBy.isBlank() || !new String(Base64.getDecoder().decode(requestedBy)).equals(coursesURI)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ServiceResponse serviceResponse = service.deleteByCourseId(courseId);
        if (serviceResponse.isOk()) {
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
        }
    }
}
