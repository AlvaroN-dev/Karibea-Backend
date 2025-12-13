package com.microservice.catalog.domain.port.in;

import com.microservice.catalog.domain.models.Product;

import java.util.List;
import java.util.UUID;

/**
 * Input port for listing products.
 */
public interface ListProductsUseCase {

    /**
     * Query object for listing products.
     */
    record ListProductsQuery(
            UUID storeId,
            int page,
            int size) {
    }

    /**
     * Result object containing paginated products.
     */
    record ListProductsResult(
            List<Product> products,
            long totalElements,
            int totalPages,
            int currentPage) {
    }

    /**
     * Lists products with optional filtering.
     *
     * @param query the query parameters
     * @return paginated list of products
     */
    ListProductsResult execute(ListProductsQuery query);
}
