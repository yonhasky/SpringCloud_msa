package com.example.catalogService.service;

import com.example.catalogService.jpa.CatalogEntity;

public interface CatalogService {
	Iterable<CatalogEntity> getAllCatalogs();

}
