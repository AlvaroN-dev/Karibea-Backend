package com.microservice.order.application.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO for Product information.
 * Contains enriched data from external products microservice context.
 */
@Builder
public record ProductInfoResponse(
        UUID id,
        String name,
        String description,
        String brand,
        String sku,
        BigDecimal basePrice,
        String currency,
        String primaryImageUrl,
        Boolean isActive
) {
    /**
     * Creates a ProductInfoResponse with only the ID (when external data is not available).
     */
    public static ProductInfoResponse withIdOnly(UUID id) {
        return ProductInfoResponse.builder()
                .id(id)
                .build();
    }
}
