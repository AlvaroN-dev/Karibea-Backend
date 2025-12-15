package com.microservice.shopcart.infrastructure.adapters;

import com.microservice.shopcart.application.exception.ExternalServiceException;
import com.microservice.shopcart.domain.port.out.ProductServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

/**
 * Adapter for communicating with Catalog microservice via WebClient.
 */
@Component
public class ProductServiceAdapter implements ProductServicePort {

    private final WebClient webClient;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public ProductServiceAdapter(WebClient.Builder webClientBuilder,
                                  @Value("${services.catalog.url:http://localhost:8081}") String catalogUrl) {
        this.webClient = webClientBuilder
            .baseUrl(catalogUrl)
            .build();
    }

    @Override
    public ProductInfo getProduct(UUID productId, UUID variantId) {
        try {
            // Build the URI based on whether variant is provided
            String uri = variantId != null 
                ? String.format("/api/v1/products/%s/variants/%s", productId, variantId)
                : String.format("/api/v1/products/%s", productId);

            CatalogProductResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(CatalogProductResponse.class)
                .timeout(TIMEOUT)
                .block();

            if (response == null) {
                throw new ExternalServiceException("Catalog", "Product not found");
            }

            return new ProductInfo(
                response.productId(),
                response.variantId(),
                response.storeId(),
                response.name(),
                response.variantName(),
                response.sku(),
                response.imageUrl(),
                response.price(),
                response.currency(),
                response.isAvailable()
            );
        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("Catalog", "Failed to fetch product: " + e.getMessage(), e);
        }
    }

    /**
     * Response DTO from Catalog service.
     */
    private record CatalogProductResponse(
        UUID productId,
        UUID variantId,
        UUID storeId,
        String name,
        String variantName,
        String sku,
        String imageUrl,
        BigDecimal price,
        String currency,
        boolean isAvailable
    ) {}
}
