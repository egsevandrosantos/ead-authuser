package com.ead.authuser.services.interfaces;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.User;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<UserDTO> findAll(Specification<User> filtersSpec, Pageable pageable, UUID courseId);
    Optional<UserDTO> findById(UUID id);
    void deleteById(UUID id) throws IllegalArgumentException;
    UUID create(UserDTO userDTO);
    void update(UserDTO userDTO);
    void merge(UserDTO source, UserDTO dest);
    void merge(UserDTO source, UserDTO dest, Class<? extends UserDTO.UserView> view);
    boolean valid(UserDTO updatedUserDTO);
    boolean valid(UserDTO updatedUserDTO, UserDTO internalUserDTO);
}
