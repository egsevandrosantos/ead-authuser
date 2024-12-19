package com.ead.authuser.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.api.dtos.UserResponse;
import com.ead.authuser.api.mappers.UserMapper;
import com.ead.authuser.domain.services.UserService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
	private final UserService service;
	private final UserMapper mapper;
	
	@GetMapping
	public List<UserResponse> findAll() {
		return mapper.toCollectionDto(service.findAll());
	}
	
	@GetMapping("/{id}")
	public UserResponse findById(@PathVariable UUID id) {
		return mapper.toDto(service.findById(id));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable UUID id) {
		service.deleteById(id);
	}
}
