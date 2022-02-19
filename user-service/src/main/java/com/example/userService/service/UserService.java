package com.example.userService.service;

import com.example.userService.dto.UserDto;
import com.example.userService.jpa.UserEntity;

public interface UserService {
	UserDto createUser(UserDto userDto);

	UserDto getUserByUserId(String userId);

	Iterable<UserEntity> getUserByAll();

}
