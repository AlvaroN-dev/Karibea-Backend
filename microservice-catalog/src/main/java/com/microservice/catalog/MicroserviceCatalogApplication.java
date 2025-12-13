package com.microservice.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCatalogApplication.class, args);
	}

}
