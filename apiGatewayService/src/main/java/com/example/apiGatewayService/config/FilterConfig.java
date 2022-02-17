package com.example.apiGatewayService.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//application.yml 대신 자바코드로 설정하기
//@Configuration
public class FilterConfig {

//	@Bean
	public RouteLocator gatewayRouted(RouteLocatorBuilder builder) {
		return builder.routes().route(r -> r.path("/first-service/**")
				.filters(f -> f.addRequestHeader("first-request", "first-request-header")
						.addResponseHeader("first-response", "first-response-header"))
				.uri("http://localhost:8091"))
				.route(r -> r.path("/second-service/**")
						.filters(f -> f.addRequestHeader("second-request", "second-request-header")
								.addResponseHeader("second-response", "second-response-header"))
						.uri("http://localhost:8092"))
				.build();
	}
}
