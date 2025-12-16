package com.microservice.search.application.dto;

import java.util.List;

/**
 * DTO para respuestas de búsqueda.
 */
public record SearchResponseDTO(
        List<ProductDTO> products,
        long totalHits,
        int page,
        int size,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        long processingTimeMs) {

    public static SearchResponseDTO empty() {
        return new SearchResponseDTO(List.of(), 0, 0, 0, 0, false, false, 0);
    }

    public static SearchResponseDTO of(
            List<ProductDTO> products,
            long totalHits,
            int page,
            int size,
            long processingTimeMs) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalHits / size) : 0;
        boolean hasNext = page < totalPages - 1;
        boolean hasPrevious = page > 0;
        return new SearchResponseDTO(
                products, totalHits, page, size, totalPages, hasNext, hasPrevious, processingTimeMs);
    }
}
