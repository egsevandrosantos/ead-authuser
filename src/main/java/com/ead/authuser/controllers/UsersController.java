package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.services.interfaces.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id") UUID id) {
        Optional<UserDTO> user = service.findById(id);
        return user
            .map(value ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(value)
            ).orElseGet(() ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
            );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @JsonView(UserDTO.Create.class) UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        if (service.valid(userDTO)) {
            UUID id = service.create(userDTO);
            UriComponents uriComponents = uriComponentsBuilder.path("/users/{id}").buildAndExpand(id);
            return ResponseEntity
                .created(uriComponents.toUri())
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userDTO.getErrors());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID id, @RequestBody @JsonView(UserDTO.Update.class) UserDTO userDTO) {
        Optional<UserDTO> updatedUserDTOOpt = service.findById(id);
        if (updatedUserDTOOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO updatedUserDTO = updatedUserDTOOpt.get();
        service.merge(userDTO, updatedUserDTO, UserDTO.Update.class);
        if (service.valid(updatedUserDTO)) {
            service.update(updatedUserDTO);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userDTO.getErrors());
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "id") UUID id, @RequestBody @JsonView(UserDTO.UpdatePassword.class) UserDTO userDTO) {
        Optional<UserDTO> internalUserDTOOpt = service.findById(id);
        if (internalUserDTOOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO internalUserDTO = internalUserDTOOpt.get();
        UserDTO updatedUserDTO = new UserDTO();
        service.merge(internalUserDTO, updatedUserDTO);
        service.merge(userDTO, updatedUserDTO, UserDTO.UpdatePassword.class);
        if (service.valid(updatedUserDTO, internalUserDTO)) {
            service.update(updatedUserDTO);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userDTO.getErrors());
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable(value = "id") UUID id, @RequestBody @JsonView(UserDTO.UpdateImage.class) UserDTO userDTO) {
        Optional<UserDTO> updatedUserDTOOpt = service.findById(id);
        if (updatedUserDTOOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO updatedUserDTO = updatedUserDTOOpt.get();
        service.merge(userDTO, updatedUserDTO, UserDTO.UpdateImage.class);
        if (service.valid(updatedUserDTO)) {
            service.update(updatedUserDTO);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userDTO.getErrors());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(value = "id") UUID id) {
        service.deleteById(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
