package com.example.firstService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {

	Environment env;

	public FirstServiceController(Environment env) {
		this.env = env;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome to the firstService";
	}

	@GetMapping("/message")
	public String message(@RequestHeader("first-request") String header) {

		log.info(header);
		return "hello FisrtService";
	}

	@GetMapping("/check")
	public String check(HttpServletRequest request) {
		log.info("Server port : " + request.getServerPort());
		log.info("environment port : " + env.getProperty("local.server.port"));

		return "message from first service";
	}
}
