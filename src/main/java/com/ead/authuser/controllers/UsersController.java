package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.services.ServiceResponse;
import com.ead.authuser.services.interfaces.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
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
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<UserDTO> usersDTOPage = service.findAll(filtersSpec, pageable);
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
    public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") UUID id) {
        Optional<UserDTO> userDTOOpt = service.findById(id);
        return userDTOOpt
            .map(userDTO ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO)
            ).orElseGet(() ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
            );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated(UserDTO.Create.class) @JsonView(UserDTO.Create.class) UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        log.debug("POST UsersController#create received userDTO: {}", userDTO.toString());
        ServiceResponse serviceResponse = service.create(userDTO);
        if (serviceResponse.isOk()) {
            UUID id = serviceResponse.getId();
            log.info("POST UsersController#create saved user id: {}", id);
            UriComponents uriComponents = uriComponentsBuilder.path("/users/{id}").buildAndExpand(id);
            return ResponseEntity
                .created(uriComponents.toUri())
                .build();
        } else {
            log.warn("POST UsersController#create not valid: {}", serviceResponse.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serviceResponse.getErrors());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID id, @RequestBody @Validated(UserDTO.Update.class) @JsonView(UserDTO.Update.class) UserDTO userDTO) {
        log.debug("PUT UsersController#update received id: {} and userDTO: {}", id, userDTO.toString());
        
        ServiceResponse serviceResponse = service.update(id, userDTO, UserDTO.Update.class);
        if (!serviceResponse.isFound()) {
            return ResponseEntity.notFound().build();
        }

        if (serviceResponse.isOk()) {
            log.info("PUT UsersController#update saved user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("PUT UsersController#update not valid: {}", serviceResponse.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serviceResponse.getErrors());
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "id") UUID id, @RequestBody @Validated(UserDTO.UpdatePassword.class) @JsonView(UserDTO.UpdatePassword.class) UserDTO userDTO) {
        log.debug("PUT UsersController#updatePassword received id: {} and userDTO: {}", id, userDTO.toString());
        
        ServiceResponse serviceResponse = service.update(id, userDTO, UserDTO.UpdatePassword.class);
        if (!serviceResponse.isFound()) {
            return ResponseEntity.notFound().build();
        }

        if (serviceResponse.isOk()) {
            log.info("PUT UsersController#updatePassword saved user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("PUT UsersController#updatePassword not valid: {}", serviceResponse.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serviceResponse.getErrors());
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable(value = "id") UUID id, @RequestBody @Validated(UserDTO.UpdateImage.class) @JsonView(UserDTO.UpdateImage.class) UserDTO userDTO) {
        log.debug("PUT UsersController#updateImage received id: {} and userDTO: {}", id, userDTO.toString());

        ServiceResponse serviceResponse = service.update(id, userDTO, UserDTO.UpdateImage.class);
        if (!serviceResponse.isFound()) {
            return ResponseEntity.notFound().build();
        }

        if (serviceResponse.isOk()) {
            log.info("PUT UsersController#updateImage saved user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("PUT UsersController#updateImage not valid: {}", serviceResponse.getErrors());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serviceResponse.getErrors());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        log.debug("DELETE UsersController#delete received id: {}", id);

        ServiceResponse serviceResponse = service.deleteById(id);
        if (serviceResponse.isOk()) {    
            log.info("DELETE UsersController#delete deleted user id: {}", id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        } else {
            log.warn("DELETE UsersController#delete id is null");
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
        }
    }
}
