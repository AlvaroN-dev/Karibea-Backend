package com.microservice.chatbot.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient configuration for external service calls.
 * Location: infrastructure/config - Infrastructure configuration.
 */
@Configuration
public class WebClientConfig {

    @Value("${external.identity.base-url:http://microservice-identity:8081}")
    private String identityServiceBaseUrl;

    @Bean
    public WebClient identityWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(identityServiceBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
