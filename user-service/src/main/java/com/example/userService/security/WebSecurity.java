package com.example.userService.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.userService.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private UserService userSerivce;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private Environment env;

	public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.env = env;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userSerivce = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/users/**").permitAll().and().addFilter(getAuthenticationFilter());
		// http.authorizeRequests().antMatchers("/**").hasIpAddress("192.168.25.28").and()
		// .addFilter(getAuthenticationFilter());// ip변경
		http.headers().frameOptions().disable(); // h2 프레임옵션 오류막기
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(userSerivce, env, authenticationManager());
//		authenticationFilter.setAuthenticationManager(authenticationManager());
		return authenticationFilter;
	}

	/**
	 * 패스워드 인코딩
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSerivce).passwordEncoder(bCryptPasswordEncoder);
	}

}
