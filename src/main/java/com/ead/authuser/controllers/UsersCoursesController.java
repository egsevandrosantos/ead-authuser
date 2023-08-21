package com.ead.authuser.controllers;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.services.interfaces.UserCourseService;
import com.ead.authuser.services.interfaces.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users/{userId}/courses")
@Log4j2
public class UsersCoursesController {
    @Autowired
    private UserCourseService service;
    @Autowired
    private UserService userService;
    @Value("${ead.api.url.course}")
    private String coursesURI;

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> findAll(
        @PathVariable(value = "userId") UUID userId,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(service.findAll(pageable, userId));
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

        Optional<UserDTO> userDTOOptional = userService.findById(userId);
        if (userDTOOptional.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        }

        UserDTO userDTO = userDTOOptional.get();
        userCourseDTO.setUserDTO(userDTO);
        if (service.valid(userCourseDTO)) {
            service.create(userCourseDTO);
            UriComponents uriComponents = uriComponentsBuilder
                .path("/users/{userId}/courses/{courseId}")
                .buildAndExpand(userId, userCourseDTO.getCourseId());
            return ResponseEntity
                .created(uriComponents.toUri())
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userCourseDTO.getErrors());
        }
    }
}
