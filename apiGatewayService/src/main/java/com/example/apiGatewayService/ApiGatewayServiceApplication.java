package com.example.apiGatewayService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServiceApplication.class, args);
	}

	/**
	 * acturator의 httptrace 사용하기위해 bean설정
	 * @return
	 */
	@Bean
	public HttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

}
