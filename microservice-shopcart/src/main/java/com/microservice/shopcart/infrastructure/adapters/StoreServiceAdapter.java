package com.microservice.shopcart.infrastructure.adapters;

import com.microservice.shopcart.domain.port.out.StoreServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.UUID;

/**
 * Adapter for communicating with Store microservice via WebClient.
 */
@Component
public class StoreServiceAdapter implements StoreServicePort {

    private final WebClient webClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public StoreServiceAdapter(WebClient.Builder webClientBuilder,
                                @Value("${services.store.url:http://localhost:8084}") String storeUrl) {
        this.webClient = webClientBuilder
            .baseUrl(storeUrl)
            .build();
    }

    @Override
    public StoreInfo getStore(UUID storeId) {
        try {
            StoreServiceResponse response = webClient.get()
                .uri("/api/v1/stores/{id}", storeId)
                .retrieve()
                .bodyToMono(StoreServiceResponse.class)
                .timeout(TIMEOUT)
                .block();

            if (response == null) {
                return null;
            }

            return new StoreInfo(
                response.storeId(),
                response.name(),
                response.logoUrl(),
                response.email()
            );
        } catch (Exception e) {
            // Return null if service is unavailable
            return null;
        }
    }

    private record StoreServiceResponse(
        UUID storeId,
        String name,
        String logoUrl,
        String email
    ) {}
}
