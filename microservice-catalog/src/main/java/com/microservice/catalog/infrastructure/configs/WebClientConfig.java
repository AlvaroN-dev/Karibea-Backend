package com.microservice.catalog.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient configuration for asynchronous HTTP communication.
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * WebClient for Store service communication.
     */
    @Bean
    public WebClient storeServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://microservice-store")
                .build();
    }
}
