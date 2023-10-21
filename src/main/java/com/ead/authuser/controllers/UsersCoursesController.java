package com.ead.authuser.controllers;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.services.ServiceResponse;
import com.ead.authuser.services.interfaces.UserCourseService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users/{userId}/courses")
@Deprecated
public class UsersCoursesController {
    @Autowired
    private UserCourseService service;
    @Value("${ead.api.url.course}")
    private String coursesURI;

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> findAll(
        @PathVariable(value = "userId") UUID userId,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<CourseDTO> coursesDTO = service.findAll(pageable, userId);

        if (coursesDTO != null) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(coursesDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(
        @PathVariable(value = "userId") UUID userId,
        @RequestBody @Validated(UserCourseDTO.Create.class) @JsonView(UserCourseDTO.Create.class) UserCourseDTO userCourseDTO,
        UriComponentsBuilder uriComponentsBuilder,
        HttpServletRequest request
    ) {
        String requestedBy = request.getHeader("Requested-By");
        if (requestedBy == null || requestedBy.isBlank() || !new String(Base64.getDecoder().decode(requestedBy)).equals(coursesURI)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ServiceResponse serviceResponse = service.create(userId, userCourseDTO);

        if (!serviceResponse.isFound()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        }

        if (serviceResponse.isOk()) {
            UriComponents uriComponents = uriComponentsBuilder
                .path("/users/{userId}/courses/{courseId}")
                .buildAndExpand(userId, userCourseDTO.getCourseId());
            return ResponseEntity
                .created(uriComponents.toUri())
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serviceResponse.getErrors());
        }
    }
}
