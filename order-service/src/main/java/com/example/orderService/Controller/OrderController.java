package com.example.orderService.Controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderService.dto.OrderDto;
import com.example.orderService.jpa.OrderEntity;
import com.example.orderService.service.OrderService;
import com.example.orderService.vo.RequestOrder;
import com.example.orderService.vo.ResponseOrder;

@RestController
@RequestMapping("/order-service")
public class OrderController {
	Environment env;
	OrderService orderService;

	@Autowired
	public OrderController(Environment env, OrderService orderService) {
		this.orderService = orderService;
		this.env = env;
	}

	@GetMapping("/health-check")
	public String status() {
		return String.format("Its Working in Order Status on PORT %s", env.getProperty("local.server.port"));
	}

	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
			@RequestBody RequestOrder order) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		OrderDto orderDto = mapper.map(order, OrderDto.class);
		orderDto.setUserId(userId);
		OrderDto createOrder = orderService.createOrder(orderDto);

		ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class); // 응답객체

		return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
		Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

		List<ResponseOrder> result = new ArrayList();
		orderList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseOrder.class));
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
}
