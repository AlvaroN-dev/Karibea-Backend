package com.microservice.search.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO para solicitudes de búsqueda.
 */
public record SearchRequestDTO(
        @NotBlank(message = "Query cannot be blank") @Size(min = 1, max = 200, message = "Query must be between 1 and 200 characters") String query,

        @Min(value = 0, message = "Page must be >= 0") int page,

        @Min(value = 1, message = "Size must be >= 1") @Max(value = 100, message = "Size must be <= 100") int size,

        String category,
        String brand,
        String storeId,
        List<String> filters,
        String sortBy,
        String sortOrder) {

    public SearchRequestDTO {
        if (page < 0)
            page = 0;
        if (size <= 0)
            size = 20;
        if (size > 100)
            size = 100;
    }

    public static SearchRequestDTO simple(String query) {
        return new SearchRequestDTO(query, 0, 20, null, null, null, null, null, null);
    }
}
