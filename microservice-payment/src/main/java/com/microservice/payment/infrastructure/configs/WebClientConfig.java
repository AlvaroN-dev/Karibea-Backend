package com.microservice.payment.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient configuration for external service communication.
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient stripeWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.stripe.com")
                .build();
    }
}
