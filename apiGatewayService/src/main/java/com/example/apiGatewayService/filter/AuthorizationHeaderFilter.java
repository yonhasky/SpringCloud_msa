package com.example.apiGatewayService.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	Environment env;

	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}

	public static class Config {

	}

	/**
	 * 토큰검증
	 */
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			
			//Authorization정보 유무 확인
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.replace("Bearer", "");

			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT is not valid", HttpStatus.UNAUTHORIZED);
			}

			return chain.filter(exchange);
		});
	}

	// jwt검증
	private boolean isJwtValid(String jwt) {
		boolean returnValue = true;
		String subject = null;

		try {
			subject = Jwts.parser().setSigningKey(env.getProperty("token.secret")) // 복호화
					.parseClaimsJws(jwt).getBody().getSubject();
			System.out.println("subject :: "+subject);
			log.error("subject LLL "+ subject);
		} catch (Exception e) {
			returnValue = false;
		}
		
		if(subject == null || subject.isEmpty()) {
			returnValue = false;
		}
		
		return returnValue;
	}

	// Mono : spring Webflux 단일값 (Mono:단일값, Flux:다중값)
	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		log.error(err);

		return response.setComplete(); // Mono타입 반환

	}

}
