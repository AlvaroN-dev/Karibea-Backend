package com.microservice.shopcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceshopcartApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceshopcartApplication.class, args);
	}

}
