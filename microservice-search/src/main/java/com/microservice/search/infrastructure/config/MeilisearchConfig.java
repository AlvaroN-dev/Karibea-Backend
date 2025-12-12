package com.microservice.search.infrastructure.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Meilisearch.
 */
@Configuration
public class MeilisearchConfig {

    @Value("${meilisearch.host:http://localhost:7700}")
    private String host;

    @Value("${meilisearch.api-key:}")
    private String apiKey;

    @Bean
    public Client meilisearchClient() {
        Config config = new Config(host, apiKey);
        return new Client(config);
    }
}
