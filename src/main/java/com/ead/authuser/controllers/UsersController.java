package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.services.interfaces.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
@Log4j2
public class UsersController {
//    Logger logger = LogManager.getLogger(UsersController.class);

    @Autowired
    private UserService service;

//    @GetMapping("/logging")
//    public String logging() {
//        log.trace("TRACE");
//        log.debug("DEBUG");
//        log.info("INFO");
//        log.warn("WARN");
//        log.error("ERROR");
//        return "Logging in Spring Boot...";
//    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
        SpecificationTemplate.UserSpec filtersSpec,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) UUID courseId
    ) {
        Page<UserDTO> usersDTOPage = service.findAll(filtersSpec, pageable, courseId);
        if (usersDTOPage.hasContent()) {
            usersDTOPage.getContent().forEach(userDTO -> {
                Link selfLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UsersController.class).findById(userDTO.getId())
                ).withSelfRel();
                userDTO.add(selfLink);
            });
        }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(usersDTOPage);
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
    public ResponseEntity<?> create(@RequestBody @Validated(UserDTO.Create.class) @JsonView(UserDTO.Create.class) UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        log.debug("POST UsersController#create received userDTO: {}", userDTO.toString());
        if (service.valid(userDTO)) {
            UUID id = service.create(userDTO);
            log.info("POST UsersController#create saved user id: {}", id);
            UriComponents uriComponents = uriComponentsBuilder.path("/users/{id}").buildAndExpand(id);
            return ResponseEntity
                .created(uriComponents.toUri())
                .build();
        } else {
            log.warn("POST UsersController#create not valid: {}", userDTO.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userDTO.getErrors());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID id, @RequestBody @Validated(UserDTO.Update.class) @JsonView(UserDTO.Update.class) UserDTO userDTO) {
        log.debug("PUT UsersController#update received id: {} and userDTO: {}", id, userDTO.toString());
        Optional<UserDTO> updatedUserDTOOpt = service.findById(id);
        if (updatedUserDTOOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO updatedUserDTO = updatedUserDTOOpt.get();
        service.merge(userDTO, updatedUserDTO, UserDTO.Update.class);
        if (service.valid(updatedUserDTO)) {
            service.update(updatedUserDTO);
            log.info("PUT UsersController#update saved user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("PUT UsersController#update not valid: {}", updatedUserDTO.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(updatedUserDTO.getErrors());
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "id") UUID id, @RequestBody @Validated(UserDTO.UpdatePassword.class) @JsonView(UserDTO.UpdatePassword.class) UserDTO userDTO) {
        log.debug("PUT UsersController#updatePassword received id: {} and userDTO: {}", id, userDTO.toString());
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
            log.info("PUT UsersController#updatePassword saved user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("PUT UsersController#updatePassword not valid: {}", updatedUserDTO.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(updatedUserDTO.getErrors());
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable(value = "id") UUID id, @RequestBody @Validated(UserDTO.UpdateImage.class) @JsonView(UserDTO.UpdateImage.class) UserDTO userDTO) {
        log.debug("PUT UsersController#updateImage received id: {} and userDTO: {}", id, userDTO.toString());
        Optional<UserDTO> updatedUserDTOOpt = service.findById(id);
        if (updatedUserDTOOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO updatedUserDTO = updatedUserDTOOpt.get();
        service.merge(userDTO, updatedUserDTO, UserDTO.UpdateImage.class);
        if (service.valid(updatedUserDTO)) {
            service.update(updatedUserDTO);
            log.info("PUT UsersController#updateImage saved user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("PUT UsersController#updateImage not valid: {}", updatedUserDTO.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(updatedUserDTO.getErrors());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        try {
            log.debug("DELETE UsersController#delete received id: {}", id);
            service.deleteById(id);
            log.info("DELETE UsersController#delete deleted user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } catch (IllegalArgumentException ex) {
            log.warn("DELETE UsersController#delete not found");
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        }
    }
}
