package com.microservice.search.infrastructure.adapters;

import com.microservice.search.application.dto.CatalogProductDTO;
import com.microservice.search.domain.port.out.CatalogClientPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

/**
 * Adaptador WebClient para comunicación con el microservicio de catálogo.
 */
@Component
public class CatalogWebClientAdapter implements CatalogClientPort {

    private static final Logger log = LoggerFactory.getLogger(CatalogWebClientAdapter.class);

    private final WebClient webClient;

    public CatalogWebClientAdapter(
            WebClient.Builder webClientBuilder,
            @Value("${services.catalog.url:http://microservice-catalog}") String catalogUrl) {
        this.webClient = webClientBuilder
                .baseUrl(catalogUrl)
                .build();
    }

    @Override
    public Mono<CatalogProductDTO> getProduct(UUID productId) {
        log.debug("Fetching product from catalog: {}", productId);

        return webClient.get()
                .uri("/api/v1/products/{id}", productId)
                .retrieve()
                .bodyToMono(CatalogProductDTO.class)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(p -> log.debug("Product fetched: {}", productId))
                .doOnError(e -> log.error("Error fetching product {}: {}", productId, e.getMessage()));
    }

    @Override
    public Flux<CatalogProductDTO> getAllActiveProducts() {
        log.info("Fetching all active products from catalog");

        return webClient.get()
                .uri("/api/v1/products?active=true")
                .retrieve()
                .bodyToFlux(CatalogProductDTO.class)
                .timeout(Duration.ofMinutes(5))
                .doOnComplete(() -> log.info("Finished fetching all products"))
                .doOnError(e -> log.error("Error fetching products: {}", e.getMessage()));
    }

    @Override
    public Flux<CatalogProductDTO> getProductsByStore(UUID storeId) {
        log.info("Fetching products for store: {}", storeId);

        return webClient.get()
                .uri("/api/v1/stores/{storeId}/products", storeId)
                .retrieve()
                .bodyToFlux(CatalogProductDTO.class)
                .timeout(Duration.ofMinutes(2))
                .doOnComplete(() -> log.info("Finished fetching products for store: {}", storeId))
                .doOnError(e -> log.error("Error fetching products for store {}: {}", storeId, e.getMessage()));
    }

    @Override
    public Mono<Boolean> healthCheck() {
        return webClient.get()
                .uri("/actuator/health")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> true)
                .timeout(Duration.ofSeconds(5))
                .onErrorReturn(false);
    }
}
