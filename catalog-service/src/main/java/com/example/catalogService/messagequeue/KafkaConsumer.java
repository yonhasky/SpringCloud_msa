package com.example.catalogService.messagequeue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.catalogService.jpa.CatalogEntity;
import com.example.catalogService.jpa.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {
	CatalogRepository repository;

	@Autowired
	public KafkaConsumer(CatalogRepository repository) {
		this.repository = repository;
	}

	/**
	 * example-catalog-topic topic에 데이터가 전달되면 해당메소드 실행
	 * 
	 * @param kafkaMessage
	 */
	@KafkaListener(topics = "example-catalog-topic")
	public void updateQty(String kafkaMessage) {
		log.info("kafka Message : " + kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		CatalogEntity entity = repository.findByProductId((String) map.get("productId"));

		if (entity != null) {
			entity.setStock(entity.getStock() - (Integer) map.get("qty")); // 수량빼기
			repository.save(entity);	//수량 업데이트
		}

	}

}
