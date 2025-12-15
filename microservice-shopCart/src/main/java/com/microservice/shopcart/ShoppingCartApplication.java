package com.microservice.shopcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Boot Application entry point for the Shopping Cart microservice.
 * 
 * This microservice handles all shopping cart operations following 
 * Domain-Driven Design (DDD) and Hexagonal Architecture principles.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }
}
