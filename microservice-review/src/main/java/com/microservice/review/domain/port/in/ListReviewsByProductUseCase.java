package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;

import java.util.List;
import java.util.UUID;

/**
 * Input port for listing reviews by product.
 */
public interface ListReviewsByProductUseCase {

    /**
     * Query object for listing reviews.
     */
    record ListReviewsQuery(
            UUID productId,
            int page,
            int size) {
    }

    /**
     * Result object containing paginated reviews.
     */
    record ListReviewsResult(
            List<ProductReview> reviews,
            long totalElements,
            int totalPages,
            int currentPage,
            Double averageRating) {
    }

    /**
     * Lists reviews for a product.
     */
    ListReviewsResult execute(ListReviewsQuery query);
}
