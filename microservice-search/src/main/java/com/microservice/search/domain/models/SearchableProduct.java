package com.microservice.search.domain.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Modelo de dominio para productos buscables.
 * Inmutable siguiendo principios DDD.
 */
public record SearchableProduct(
        ProductId id,
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
        boolean isAvailable,
        Integer stockQuantity,
        String primaryImageUrl,
        List<String> imageUrls,
        boolean isActive,
        Instant indexedAt,
        Instant updatedAt,
        boolean isDeleted) {

    public SearchableProduct {
        Objects.requireNonNull(id, "Product ID cannot be null");
        Objects.requireNonNull(name, "Product name cannot be null");
        categoryNames = categoryNames != null ? List.copyOf(categoryNames) : List.of();
        categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
        colors = colors != null ? List.copyOf(colors) : List.of();
        sizes = sizes != null ? List.copyOf(sizes) : List.of();
        imageUrls = imageUrls != null ? List.copyOf(imageUrls) : List.of();
    }

    /**
     * Factory method para crear un producto nuevo.
     */
    public static SearchableProduct create(
            UUID externalProductId,
            UUID storeId,
            String name,
            String description,
            String brand,
            List<String> categoryNames,
            BigDecimal price,
            String primaryImageUrl) {
        return new SearchableProduct(
                new ProductId(externalProductId),
                storeId,
                name,
                description,
                brand,
                categoryNames,
                List.of(),
                price,
                null,
                "COP",
                List.of(),
                List.of(),
                null,
                0,
                0,
                0,
                true,
                0,
                primaryImageUrl,
                List.of(),
                true,
                Instant.now(),
                null,
                false);
    }

    /**
     * Marca el producto como eliminado.
     */
    public SearchableProduct markAsDeleted() {
        return new SearchableProduct(
                id, storeId, name, description, brand, categoryNames, categoryIds,
                price, compareAtPrice, currency, colors, sizes, averageRating,
                reviewCount, salesCount, viewCount, false, stockQuantity,
                primaryImageUrl, imageUrls, false, indexedAt, Instant.now(), true);
    }

    /**
     * Actualiza el timestamp de indexación.
     */
    public SearchableProduct withUpdatedTimestamp() {
        return new SearchableProduct(
                id, storeId, name, description, brand, categoryNames, categoryIds,
                price, compareAtPrice, currency, colors, sizes, averageRating,
                reviewCount, salesCount, viewCount, isAvailable, stockQuantity,
                primaryImageUrl, imageUrls, isActive, indexedAt, Instant.now(), isDeleted);
    }

    /**
     * Verifica si tiene descuento.
     */
    public boolean hasDiscount() {
        return compareAtPrice != null && price != null && compareAtPrice.compareTo(price) > 0;
    }

    /**
     * Calcula el porcentaje de descuento.
     */
    public int discountPercentage() {
        if (!hasDiscount())
            return 0;
        return compareAtPrice.subtract(price)
                .multiply(BigDecimal.valueOf(100))
                .divide(compareAtPrice, 0, java.math.RoundingMode.HALF_UP)
                .intValue();
    }
}
