package com.microservice.search.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO para productos recibidos del microservicio de catálogo.
 */
public record CatalogProductDTO(
        UUID id,
        UUID storeId,
        String name,
        String description,
        String brand,
        List<String> categoryNames,
        List<UUID> categoryIds,
        BigDecimal price,
        BigDecimal compareAtPrice,
        String currency,
        List<String> colors,
        List<String> sizes,
        BigDecimal averageRating,
        Integer reviewCount,
        Integer salesCount,
        Integer viewCount,
        Boolean isAvailable,
        Integer stockQuantity,
        String primaryImageUrl,
        List<String> imageUrls,
        Boolean isActive) {
}
