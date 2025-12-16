package com.microservice.marketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceMarketingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceMarketingApplication.class, args);
	}

}
