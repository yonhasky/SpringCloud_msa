package com.example.orderService.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orderService.dto.OrderDto;
import com.example.orderService.jpa.OrderEntity;
import com.example.orderService.jpa.OrderRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	// 유저 등록
	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDto.getQty()*orderDto.getUnitPrice());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

		orderRepository.save(orderEntity);

		OrderDto returnOrderDto = mapper.map(orderEntity, OrderDto.class);

		return returnOrderDto;
	}

	/**
	 * 단일주문 조회
	 */
	@Override
	public OrderDto getOrderByOrderId(String orderId) {
		OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
		OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);
		
		return orderDto;
	}

	@Override
	public Iterable<OrderEntity> getOrderByUserId(String userId) {
		return orderRepository.findByUserId(userId);
	}

}
