package com.microservice.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceShippingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceShippingApplication.class, args);
	}

}
