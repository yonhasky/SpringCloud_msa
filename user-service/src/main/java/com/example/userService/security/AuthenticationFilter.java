package com.example.userService.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.userService.dto.UserDto;
import com.example.userService.service.UserService;
import com.example.userService.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private UserService userService;
	private Environment env;

	public AuthenticationFilter(UserService userService, Environment env, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.env = env;

		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {

			RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 인증 성공 후 호출됨
	 * 인증 성공 후 토큰 발행
	 * JWT : 클라이언트 독립적 서비스(stateless), CDN 캐쉬서버 인증가능, No Cookie-Session, 지속적인 토큰 저장
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String userName = ((User) authResult.getPrincipal()).getUsername();
		UserDto userDetails = userService.getUserDetailByEmail(userName);

		//jwt 토큰 생성
		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())  //토큰생성할내용
				.setExpiration(
						new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))  //토큰유효기간
				.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))  //토큰 암호화방식
				.compact();

		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId()); //위변조 확인용으로 같이던짐
		
		
		log.info(((User) authResult.getPrincipal()).getUsername());
	}

}
