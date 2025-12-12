package com.microservice.user.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de WebClient para comunicación con otros microservicios
 */
@Configuration
public class WebClientConfig {
    
    @Value("${identity.service.url:http://microservice-identity:8080}")
    private String identityServiceUrl;
    
    @Bean
    public WebClient identityWebClient() {
        return WebClient.builder()
            .baseUrl(identityServiceUrl)
            .build();
    }
}
