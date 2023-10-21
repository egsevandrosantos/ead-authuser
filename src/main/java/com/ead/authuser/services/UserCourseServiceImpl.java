package com.ead.authuser.services;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.interfaces.UserCourseService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

@Service
@Deprecated
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UserCourseRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseClient courseClient;

    @Override
    public Page<CourseDTO> findAll(Pageable pageable, UUID userId) {
        return courseClient.findAllCourses(pageable, userId);
    }

    @Override
    public ServiceResponse create(UUID userId, UserCourseDTO userCourseDTO) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ServiceResponse.builder()
                .ok(false)
                .found(false)
                .build();
        }

        UserCourse userCourse = new UserCourse();
        userCourse.setUser(userOpt.get());
        merge(userCourseDTO, userCourse);
        Map<String, List<String>> errors = valid(userCourse);
        if (!errors.isEmpty()) {
            return ServiceResponse.builder()
                .ok(false)
                .found(true)
                .errors(errors)
                .build();
        }

        userCourse = repository.save(userCourse);
        return ServiceResponse.builder()
            .id(userCourse.getId())
            .build();
    }

    @Override
    @Transactional
    public ServiceResponse deleteByCourseId(UUID courseId) {
        if (courseId == null) {
            return ServiceResponse.builder()
                .ok(false)
                .found(false)
                .build();
        }
        
        if (repository.existsByCourseId(courseId)) {
            repository.deleteAllByCourseId(courseId);
        }
        return ServiceResponse.builder().build();
    }

    private Map<String, List<String>> valid(UserCourse userCourse) {
        Map<String, List<String>> errors = new HashMap<>();
        if (userCourse.getUser() == null) {
            errors.put("user", List.of("User not exists"));
        } else {
            User user = new User();
            user.setId(userCourse.getUser().getId());
            if (repository.existsByUserAndCourseId(user, userCourse.getCourseId())) {
                errors.put("userCourse", List.of("Relationship already exists"));
            }
        }

        return errors;
    }

    public void merge(UserCourseDTO source, UserCourseDTO dest) {
        BeanUtils.copyProperties(source, dest);
    }

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
    }
}
