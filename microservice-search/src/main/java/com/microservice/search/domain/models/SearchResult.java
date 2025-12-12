package com.microservice.search.domain.models;

import java.util.List;


/**
 * Value Object que representa el resultado de una búsqueda.
 */
public record SearchResult(
        List<SearchableProduct> products,
        long totalHits,
        int page,
        int size,
        long processingTimeMs) {

    public SearchResult {
        products = products != null ? List.copyOf(products) : List.of();
    }

    /**
     * Verifica si hay más páginas disponibles.
     */
    public boolean hasNext() {
        return (long) (page + 1) * size < totalHits;
    }

    /**
     * Verifica si hay páginas anteriores.
     */
    public boolean hasPrevious() {
        return page > 0;
    }

    /**
     * Calcula el total de páginas.
     */
    public int totalPages() {
        if (size == 0)
            return 0;
        return (int) Math.ceil((double) totalHits / size);
    }

    /**
     * Factory method para resultado vacío.
     */
    public static SearchResult empty() {
        return new SearchResult(List.of(), 0, 0, 20, 0);
    }
}
