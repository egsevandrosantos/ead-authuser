package com.ead.authuser.api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
	private String username;
	private String email;
	private String name;
	private String phone;
	private String cpf;
	private String imageUrl;
	private String password;
}
