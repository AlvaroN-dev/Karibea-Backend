package com.microservice.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Microservicio de búsqueda con arquitectura hexagonal.
 * Integra Meilisearch + PostgreSQL, WebClient y Kafka.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceSearchApplication.class, args);
	}
}
