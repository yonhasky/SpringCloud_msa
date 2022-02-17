package com.example.userService.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RequestUser {

	@NotNull(message = "Email cannot be null")
	@Email
	@Size(min = 2, message = "email not be less than 2")
	private String email;

	@NotNull(message = "name cannot be null")
	@Size(min = 2, message = "name not be less than 2")
	private String name;

	@NotNull(message = "password cannot be null")
	@Size(min = 8, message = "password must be equal or grater than than 8")
	private String pwd;

}
