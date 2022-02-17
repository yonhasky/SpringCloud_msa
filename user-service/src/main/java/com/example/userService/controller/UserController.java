package com.example.userService.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userService.dto.UserDto;
import com.example.userService.service.UserService;
import com.example.userService.vo.Greeting;
import com.example.userService.vo.RequestUser;
import com.example.userService.vo.ResponseUser;

@RestController
@RequestMapping("/")
public class UserController {

	private Environment env;
	private UserService userService;

	@Autowired
	private Greeting greeting;

	@Autowired
	public UserController(Environment env, UserService userService) {
		this.env = env;
		this.userService = userService;
	}

	@GetMapping("/health-check")
	public String status() {
		return "Its Working in User Status";
	}

	@GetMapping("/welcome")
	public String welcome() {
		// return env.getProperty("greeting.message"); // application.yml파일에서 가져오는 방법
		// Environment 사용
		return greeting.getMessage(); // application.yml에서 가져오는 방법 클래스에서 @Valid사용
	}

	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = mapper.map(user, UserDto.class);
		userService.createUser(userDto);

		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);		//응답객체
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
}
