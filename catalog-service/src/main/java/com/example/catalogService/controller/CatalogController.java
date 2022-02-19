package com.example.catalogService.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogService.jpa.CatalogEntity;
import com.example.catalogService.service.CatalogService;
import com.example.catalogService.vo.ResponseCatalog;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
	Environment env;
	CatalogService catalogService;

	@Autowired
	public CatalogController(Environment env, CatalogService catalogService) {
		this.catalogService = catalogService;
		this.env = env;
	}

	@GetMapping("/health-check")
	public String status() {
		return String.format("Its Working in Catalog Status on PORT %s", env.getProperty("local.server.port"));
	}

	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getUsers() {
		Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();
		List<ResponseCatalog> result = new ArrayList<>();

		catalogList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseCatalog.class));
		});

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
