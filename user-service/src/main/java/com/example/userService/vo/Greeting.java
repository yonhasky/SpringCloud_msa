package com.example.userService.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
//@AllArgsConstructor
//@NoArgsConstructor		//default생성자
public class Greeting {
	@Value("${greeting.message}")	//application.yml에서 가져오기 @Valid사용
	private String message;

}
