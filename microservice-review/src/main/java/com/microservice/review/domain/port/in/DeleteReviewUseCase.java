package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;

import java.util.UUID;

/**
 * Input port for deleting a review (soft delete).
 */
public interface DeleteReviewUseCase {

    /**
     * Soft deletes a review.
     */
    ProductReview execute(UUID reviewId);
}
