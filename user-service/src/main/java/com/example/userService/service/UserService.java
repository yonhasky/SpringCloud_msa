package com.example.userService.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.userService.dto.UserDto;
import com.example.userService.jpa.UserEntity;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto userDto);

	UserDto getUserByUserId(String userId);

	Iterable<UserEntity> getUserByAll();

	UserDto getUserDetailByEmail(String email);

}
