package com.microservice.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.cloud.openfeign.EnableFeignClients
public class MicroserviceStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceStoreApplication.class, args);
	}

}
