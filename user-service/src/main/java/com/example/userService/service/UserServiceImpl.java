package com.example.userService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userService.dto.UserDto;
import com.example.userService.jpa.UserEntity;
import com.example.userService.jpa.UserRepository;
import com.example.userService.vo.ResponseOrder;

@Service
public class UserServiceImpl implements UserService {
	UserRepository userRepository;
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// 유저 등록
	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd())); // 패스워드 암호화

		userRepository.save(userEntity);

		UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

		return returnUserDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId); // findByUserId > UserRepository에 생성

		if (userEntity == null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

		List<ResponseOrder> orders = new ArrayList<>();
		userDto.setOrders(orders);

		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}

		// springSecurity의 User객체
		return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new ModelMapper().map(userEntity, UserDto.class);
	}

}
