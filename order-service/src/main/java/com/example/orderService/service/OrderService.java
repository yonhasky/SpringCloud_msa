package com.example.orderService.service;

import com.example.orderService.dto.OrderDto;
import com.example.orderService.jpa.OrderEntity;

public interface OrderService {
	OrderDto createOrder(OrderDto orderDetails);
	OrderDto getOrderByOrderId(String orderId);
	Iterable<OrderEntity> getOrderByUserId(String userId);

}
