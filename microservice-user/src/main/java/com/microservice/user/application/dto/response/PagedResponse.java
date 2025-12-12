package com.microservice.user.application.dto.response;

import java.util.List;

/**
 * DTO de respuesta paginada
 */
public record PagedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
) {
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, 
                                           long totalElements, int totalPages) {
        return new PagedResponse<>(
            content,
            page,
            size,
            totalElements,
            totalPages,
            page == 0,
            page >= totalPages - 1
        );
    }
}
