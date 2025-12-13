package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;

import java.util.UUID;

/**
 * Input port for getting review detail.
 */
public interface GetReviewDetailUseCase {

    /**
     * Gets the full details of a review.
     */
    ProductReview execute(UUID reviewId);
}
