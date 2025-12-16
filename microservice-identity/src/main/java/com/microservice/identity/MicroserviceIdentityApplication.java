package com.microservice.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceIdentityApplication.class, args);
	}

}
