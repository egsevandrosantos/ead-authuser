package com.ead.authuser.services;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.interfaces.UserCourseService;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UserCourseRepository repository;
    @Autowired
    private CourseClient courseClient;

    @Override
    public Page<CourseDTO> findAll(Pageable pageable, UUID userId) {
        return courseClient.findAllCourses(pageable, userId);
    }

    @Override
    public UUID create(UserCourseDTO userCourseDTO) {
        UserCourse userCourse = new UserCourse();
        merge(userCourseDTO, userCourse);
        Instant createdAt = Instant.now();
        userCourse.setCreatedAt(createdAt);
        userCourse.setUpdatedAt(createdAt);
        userCourse = repository.save(userCourse);
        return userCourse.getId();
    }

    @Override
    public boolean valid(UserCourseDTO userCourseDTO) {
        if (userCourseDTO.getUserDTO() == null) {
            userCourseDTO.getErrors().put("user", List.of("User not exists"));
        } else {
            User user = new User();
            user.setId(userCourseDTO.getUserDTO().getId());
            if (repository.existsByUserAndCourseId(user, userCourseDTO.getCourseId())) {
                userCourseDTO.getErrors().put("userCourse", List.of("Relationship already exists"));
            }
        }

        return userCourseDTO.getErrors().isEmpty();
    }

    @Override
    public void merge(UserCourseDTO source, UserCourseDTO dest) {
        BeanUtils.copyProperties(source, dest);
    }

    @Override
    public void merge(UserCourseDTO source, UserCourseDTO dest, Class<? extends UserCourseDTO.UserCourseView> view) {
        String[] fieldsNotInViewToIgnore = Arrays.stream(UserCourseDTO.class.getDeclaredFields())
            .filter(field -> {
                JsonView jsonView = field.getAnnotation(JsonView.class);
                if (jsonView == null) {
                    return true;
                }
                return Arrays.stream(jsonView.value()).noneMatch(value -> value.getName().equals(view.getName()));
            })
            .map(Field::getName)
            .toArray(String[]::new);
        BeanUtils.copyProperties(source, dest, fieldsNotInViewToIgnore);
    }

    private void merge(UserCourseDTO source, UserCourse dest) {
        BeanUtils.copyProperties(source, dest);

        User user = new User();
        BeanUtils.copyProperties(source.getUserDTO(), user);
        dest.setUser(user);
    }

    private void merge(UserCourse source, UserCourseDTO dest) {
        BeanUtils.copyProperties(source, dest);

        if (Hibernate.isInitialized(source.getUser())) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(source.getUser(), userDTO);
            dest.setUserDTO(userDTO);
        }
    }
}
