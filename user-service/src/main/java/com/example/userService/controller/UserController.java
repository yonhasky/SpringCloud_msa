package com.example.userService.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userService.dto.UserDto;
import com.example.userService.jpa.UserEntity;
import com.example.userService.service.UserService;
import com.example.userService.vo.Greeting;
import com.example.userService.vo.RequestUser;
import com.example.userService.vo.ResponseUser;

@RestController
@RequestMapping("/user-service")
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
		return String.format("Its Working in User Status on PORT %s", env.getProperty("local.server.port"));
	}

	@GetMapping("/welcome")
	public String welcome() {
		// return env.getProperty("greeting.message"); // application.yml파일에서 가져오는 방법
		// Environment 사용
		return greeting.getMessage(); // application.yml에서 가져오는 방법 클래스에서 @Valid사용
	}

	/**
	 * user 등록
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = mapper.map(user, UserDto.class);
		userService.createUser(userDto);

		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class); // 응답객체

		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}

	/**
	 * user 전체조회
	 * 
	 * @return
	 */
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers() {
		Iterable<UserEntity> userList = userService.getUserByAll();
		List<ResponseUser> result = new ArrayList<>();

		// userList값을 result에 변환해서 담기
		userList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseUser.class));
		});

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	/**
	 * user 개별조회
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId) {
		UserDto userDto = userService.getUserByUserId(userId);

		// userList값을 result에 변환해서 담기
		ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
