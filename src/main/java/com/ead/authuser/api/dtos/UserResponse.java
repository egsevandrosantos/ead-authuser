package com.ead.authuser.api.dtos;

import java.time.Instant;
import java.util.UUID;

import com.ead.authuser.domain.enumerations.UserStatus;
import com.ead.authuser.domain.enumerations.UserType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserResponse {
	@EqualsAndHashCode.Include
	private UUID id;
	private String username;
	private String email;
	private String name;
	private UserStatus status;
	private UserType type;
	private String phone;
	private String cpf;
	private String imageUrl;
	private Instant createdAt;
	private Instant updatedAt;
}
