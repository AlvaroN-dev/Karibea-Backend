package com.microservice.search.domain.models;

import java.util.List;

/**
 * Value Object que encapsula los parámetros de una consulta de búsqueda.
 */
public record SearchQuery(
        String term,
        int page,
        int size,
        String category,
        List<String> filters) {

    public SearchQuery {
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Size cannot exceed 100");
        }
        filters = filters != null ? List.copyOf(filters) : List.of();
    }

    /**
     * Calcula el offset para paginación.
     */
    public int offset() {
        return page * size;
    }

    /**
     * Factory method para búsqueda simple.
     */
    public static SearchQuery simple(String term) {
        return new SearchQuery(term, 0, 20, null, List.of());
    }

    /**
     * Factory method con paginación.
     */
    public static SearchQuery of(String term, int page, int size) {
        return new SearchQuery(term, page, size, null, List.of());
    }
}
