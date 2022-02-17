package com.example.secondService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/second-service")
@Slf4j
public class SecondServiceController {

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome to the SecondService";
	}

	@GetMapping("/message")
	public String message(@RequestHeader("second-request") String header) {
		log.info(header);
		return "hello secondService";
	}

	@GetMapping("/check")
	public String check() {

		return "message from second service";
	}
}
