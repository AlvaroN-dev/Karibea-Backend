package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;

import java.util.UUID;

/**
 * Input port for reporting a review.
 */
public interface ReportReviewUseCase {

    /**
     * Reports a review as inappropriate.
     */
    ProductReview execute(UUID reviewId);
}
