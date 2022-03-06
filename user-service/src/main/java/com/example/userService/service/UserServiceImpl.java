package com.example.userService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.userService.client.OrderServiceClient;
import com.example.userService.dto.UserDto;
import com.example.userService.jpa.UserEntity;
import com.example.userService.jpa.UserRepository;
import com.example.userService.vo.ResponseOrder;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	UserRepository userRepository;
	BCryptPasswordEncoder passwordEncoder;

	Environment env;
	RestTemplate restTemplate;
	OrderServiceClient orderServiceClient;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env,
			RestTemplate restTemplate, OrderServiceClient orderServiceClient) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.env = env;
		this.restTemplate = restTemplate;
		this.orderServiceClient = orderServiceClient;
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

//		List<ResponseOrder> orders = new ArrayList<>();
		
// Using as rest template
//		String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//		ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//				new ParameterizedTypeReference<List<ResponseOrder>>() {
//				});
//
//		List<ResponseOrder> orderList = orderListResponse.getBody();
		
		//feignClient 사용
//		List<ResponseOrder> orderList = null;
//		try {
//			orderList = orderServiceClient.getOrders(userId);
//		}catch(FeignException e) {
//			log.error(e.getMessage());
//		}
		
		/*ErrorDecoder 사용*/
		List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);	
		userDto.setOrders(orderList);

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
