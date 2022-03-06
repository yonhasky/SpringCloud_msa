package com.example.catalogService.messagequeue;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;


@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGruopId"); // consumer들을 그룹핑하는 ID
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // KEY 직렬화
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // VALUE

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	/**
	 * 리스너
	 * @return
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		return kafkaListenerContainerFactory;

	}

}
