package com.ead.authuser.api.mappers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ead.authuser.api.dtos.UserResponse;
import com.ead.authuser.domain.models.User;

@Component
public class UserMapper {
	public UserResponse toDto(User user) {
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(user, userResponse);
		return userResponse;
	}
	
	public List<UserResponse> toCollectionDto(List<User> users) {
		return users.stream().map(this::toDto).toList();
	}
}
