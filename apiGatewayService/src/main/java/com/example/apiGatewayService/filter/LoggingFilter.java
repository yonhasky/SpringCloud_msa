package com.example.apiGatewayService.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter
//		return (exchange, chain) -> {
//			ServerHttpRequest request = exchange.getRequest();
//			ServerHttpResponse response = exchange.getResponse();
//
//			log.info("GlobalFilter getId : " + config.getBaseMessage());
//
//			if (config.isPreLogger()) {
//				log.info("Global filter start" + request.getId());
//			}
//			// Custom Post Filter
//			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//				if (config.isPostLogger()) {
//					log.info("GlobalFilter statusCode : " + response.getStatusCode());
//				}
//			}));
//		};

		GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			log.info("Logging Filter getId : " + config.getBaseMessage());

			if (config.isPreLogger()) {
				log.info("Logging Pre filter start" + request.getId());
			}
			// Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Logging Post Filter statusCode : " + response.getStatusCode());
				}
			}));

			// 우선순위 HIGHEST_PRECEDENCE
		}, Ordered.HIGHEST_PRECEDENCE);
		return filter;
	}

	@Data
	public static class Config {
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}

}
