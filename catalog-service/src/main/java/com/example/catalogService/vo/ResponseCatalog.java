package com.example.catalogService.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseCatalog {
	private String productId;
	private String productName;
	private Integer unitPrice;
	private Integer stock;
	private Date createAt;
}
