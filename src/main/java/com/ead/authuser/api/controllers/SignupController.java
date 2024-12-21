package com.ead.authuser.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ead.authuser.api.dtos.UserRequest;
import com.ead.authuser.api.mappers.UserMapper;
import com.ead.authuser.domain.services.SignupService;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/signup")
@AllArgsConstructor
public class SignupController {
	private final UserMapper mapper;
	private final SignupService service;

	@PostMapping
	public ResponseEntity<Void> signup(@RequestBody UserRequest user) {
		UUID id = service.signup(mapper.toEntity(user));
		return ResponseEntity.created(
			MvcUriComponentsBuilder.fromMethodCall(
				MvcUriComponentsBuilder
					.on(UsersController.class)
					.findById(id)
			)
			.build().encode().toUri()
		).build();
	}
	
}
