package com.microservice.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the Chatbot Microservice.
 * Implements RAG (Retrieval-Augmented Generation) with Hexagonal Architecture.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceChatbotApplication.class, args);
	}
}
