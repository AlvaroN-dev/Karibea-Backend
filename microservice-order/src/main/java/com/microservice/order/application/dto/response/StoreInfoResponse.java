package com.microservice.order.application.dto.response;

import lombok.Builder;

import java.util.UUID;

/**
 * Response DTO for Store information.
 * Contains enriched data from external stores microservice context.
 */
@Builder
public record StoreInfoResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String logoUrl
) {
    /**
     * Creates a StoreInfoResponse with only the ID (when external data is not available).
     */
    public static StoreInfoResponse withIdOnly(UUID id) {
        return StoreInfoResponse.builder()
                .id(id)
                .build();
    }
}
