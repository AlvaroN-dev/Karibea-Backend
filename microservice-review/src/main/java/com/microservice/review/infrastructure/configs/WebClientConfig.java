package com.microservice.review.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient configuration for reactive HTTP communication.
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * WebClient for Order service communication (to verify purchases).
     */
    @Bean
    public WebClient orderServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://microservice-order")
                .build();
    }

    /**
     * WebClient for User service communication.
     */
    @Bean
    public WebClient userServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://microservice-identity")
                .build();
    }
}
