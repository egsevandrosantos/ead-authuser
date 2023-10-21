package com.ead.authuser.services.interfaces;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.User;
import com.ead.authuser.services.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<UserDTO> findAll(Specification<User> filtersSpec, Pageable pageable);
    Optional<UserDTO> findById(UUID id);
    ServiceResponse deleteById(UUID id) throws IllegalArgumentException;
    ServiceResponse create(UserDTO userDTO);
    ServiceResponse update(UUID id, UserDTO userDTO, Class<? extends UserDTO.UserView> view);
}
