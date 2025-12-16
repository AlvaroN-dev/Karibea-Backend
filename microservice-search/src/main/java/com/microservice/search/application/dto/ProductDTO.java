package com.microservice.search.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO para representación de productos en respuestas.
 */
public record ProductDTO(
        UUID id,
        UUID storeId,
        String name,
        String description,
        String brand,
        List<String> categories,
        BigDecimal price,
        BigDecimal compareAtPrice,
        String currency,
        BigDecimal rating,
        Integer reviewCount,
        Boolean isAvailable,
        String primaryImageUrl,
        List<String> imageUrls) {
}
