package com.ead.authuser.services.interfaces;

import com.ead.authuser.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<UserDTO> findAll(Pageable pageable);
    Optional<UserDTO> findById(UUID id);
    void deleteById(UUID id);
    UUID create(UserDTO userDTO);
    void update(UserDTO userDTO);
    void merge(UserDTO source, UserDTO dest);
    void merge(UserDTO source, UserDTO dest, Class<? extends UserDTO.UserView> view);
    boolean valid(UserDTO updatedUserDTO);
    boolean valid(UserDTO updatedUserDTO, UserDTO internalUserDTO);
}
