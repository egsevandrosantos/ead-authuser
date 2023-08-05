package com.ead.authuser.services;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.User;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.interfaces.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public Page<UserDTO> findAll(SpecificationTemplate.UserSpec filtersSpec, Pageable pageable) {
        Page<User> usersPage = repository.findAll(filtersSpec, pageable);

        List<User> users = usersPage.getContent();
        List<UserDTO> usersDTO = new ArrayList<>();
        users.forEach(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            usersDTO.add(userDTO);
        });

        return new PageImpl<>(usersDTO, usersPage.getPageable(), usersPage.getTotalElements());
    }

    @Override
    public Optional<UserDTO> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user.get(), userDTO);
        return Optional.of(userDTO);
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            return;
        }
        repository.deleteById(id);
    }

    @Override
    public UUID create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setStatus(UserStatus.ACTIVE);
        user.setType(UserType.STUDENT);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user = repository.save(user);
        return user.getId();
    }

    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdatedAt(Instant.now());
        repository.save(user);
    }

    @Override
    public void merge(UserDTO source, UserDTO dest) {
        BeanUtils.copyProperties(source, dest);
    }

    @Override
    public void merge(UserDTO source, UserDTO dest, Class<? extends UserDTO.UserView> view) {
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

    public boolean valid(UserDTO updatedUserDTO) {
        return valid(updatedUserDTO, null);
    }

    public boolean valid(UserDTO updatedUserDTO, UserDTO internalUserDTO) {
        boolean usernameExists = updatedUserDTO.getId() == null
            ? repository.existsByUsername(updatedUserDTO.getUsername())
            : repository.existsByUsernameAndIdIsNot(updatedUserDTO.getUsername(), updatedUserDTO.getId());
        if (usernameExists) {
            updatedUserDTO.getErrors().put("username", List.of("Username is already taken."));
        }

        boolean emailExists = updatedUserDTO.getId() == null
            ? repository.existsByEmail(updatedUserDTO.getEmail())
            : repository.existsByEmailAndIdIsNot(updatedUserDTO.getEmail(), updatedUserDTO.getId());
        if (emailExists) {
            updatedUserDTO.getErrors().put("email", List.of("Email is already taken."));
        }

        if (internalUserDTO != null) {
            if (updatedUserDTO.getOldPassword() != null
                && !updatedUserDTO.getOldPassword().isBlank()
                && !updatedUserDTO.getOldPassword().equals(internalUserDTO.getPassword())
            ) {
                updatedUserDTO.getErrors().put("oldPassword", List.of("Wrong password."));
            }
        }

        return updatedUserDTO.getErrors().isEmpty();
    }
}
