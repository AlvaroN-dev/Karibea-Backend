package com.microservice.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceReviewApplication.class, args);
	}

}
