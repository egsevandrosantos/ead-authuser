package com.ead.authuser.services;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.User;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.interfaces.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private CourseClient courseClient;

    @Override
    public Page<UserDTO> findAll(Specification<User> filtersSpec, Pageable pageable, UUID courseId) {
        if (courseId != null) {
            filtersSpec = ((Specification<User>) (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.join("usersCourses").get("courseId"), courseId))
            ).and(filtersSpec);
        }
        Page<User> usersPage = repository.findAll(filtersSpec, pageable);

        List<User> users = usersPage.getContent();
        List<UserDTO> usersDTO = users.stream().map(user -> merge(user, new UserDTO())).collect(Collectors.toList());
        return new PageImpl<>(usersDTO, usersPage.getPageable(), usersPage.getTotalElements());
    }

    @Override
    public Optional<UserDTO> findById(UUID id) {
        Optional<User> userOpt;
        if (id == null || (userOpt = repository.findById(id)).isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        return Optional.of(merge(user, new UserDTO()));
    }

    @Override
    @Transactional
    public ServiceResponse deleteById(UUID id) throws IllegalArgumentException {
        if (id == null) {
            return ServiceResponse.builder()
                .ok(false)
                .build();
        }
        if (repository.existsById(id)) {
            userCourseRepository.deleteAllByUser(id);
            repository.deleteById(id);
            courseClient.deleteCourseUserRelationship(id);
        }
        return ServiceResponse.builder().build();
    }

    @Override
    public ServiceResponse create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        Map<String, List<String>> errors = valid(user);
        if (!errors.isEmpty()) {
            return ServiceResponse.builder()
                .ok(false)
                .errors(errors)
                .build();
        }

        user.setStatus(UserStatus.ACTIVE);
        user = repository.save(user);
        return ServiceResponse.builder().id(user.getId()).build();
    }

    @Override
    @Transactional // Evitar a consulta SELECT antes do UPDATE
    public ServiceResponse update(UUID id, UserDTO userDTO, Class<? extends UserDTO.UserView> view) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isEmpty()) {
            return ServiceResponse.builder()
                .ok(false)
                .found(false)
                .build();
        }

        User user = userOpt.get();
        User internalUser = merge(user, new User());
        User updatedUser = merge(user, new User());
        merge(userDTO, updatedUser, view);
        Map<String, List<String>> errors = valid(updatedUser, userDTO, internalUser);
        if (!errors.isEmpty()) {
            return ServiceResponse.builder()
                .ok(false)
                .found(true)
                .errors(errors)
                .build();
        }

        merge(userDTO, user, view);
        repository.save(user);
        return ServiceResponse.builder().build();
    }

    public UserDTO merge(User source, UserDTO dest) {
        BeanUtils.copyProperties(source, dest);
        return dest;
    }

    private User merge(User source, User dest) {
        BeanUtils.copyProperties(source, dest);
        return dest;
    }

    private void merge(UserDTO source, User dest, Class<? extends UserDTO.UserView> view) {
        String[] fieldsNotInViewToIgnore = Arrays.stream(UserDTO.class.getDeclaredFields())
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

    private Map<String, List<String>> valid(User updatedUser) {
        return valid(updatedUser, null, null);
    }

    private Map<String, List<String>> valid(User updatedUser, UserDTO userDTO, User internalUser) {
        Map<String, List<String>> errors = new HashMap<>();

        boolean usernameExists = updatedUser.getId() == null
            ? repository.existsByUsername(updatedUser.getUsername())
            : repository.existsByUsernameAndIdIsNot(updatedUser.getUsername(), updatedUser.getId());
        if (usernameExists) {
            errors.put("username", List.of("Username is already taken."));
        }

        boolean emailExists = updatedUser.getId() == null
            ? repository.existsByEmail(updatedUser.getEmail())
            : repository.existsByEmailAndIdIsNot(updatedUser.getEmail(), updatedUser.getId());
        if (emailExists) {
            errors.put("email", List.of("Email is already taken."));
        }

        List<UserType> userTypes = new ArrayList<>();
        userTypes.add(updatedUser.getType());
        if (internalUser != null) {
            userTypes.add(internalUser.getType());

            if (userDTO != null
                && userDTO.getOldPassword() != null
                && !userDTO.getOldPassword().isBlank()
                && !userDTO.getOldPassword().equals(internalUser.getPassword())
            ) {
                errors.put("oldPassword", List.of("Wrong password."));
            }
        }

        userTypes.add(UserType.STUDENT); // Default
        updatedUser.setType(ObjectUtils.firstNonNull(userTypes.toArray(new UserType[0])));

        return errors;
    }
}
