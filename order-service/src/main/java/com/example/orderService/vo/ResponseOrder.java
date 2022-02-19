package com.example.orderService.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseOrder {
	private String productId;
	private Integer qty;
	private Integer unitPrice;
	private Integer totalPrice;
	private Integer stock;
	private Date createAt;
	private String orderId;
}
