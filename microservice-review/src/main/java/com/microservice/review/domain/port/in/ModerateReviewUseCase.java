package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.ReviewStatus;

import java.util.UUID;

/**
 * Input port for moderating a review.
 */
public interface ModerateReviewUseCase {

    /**
     * Command object for moderating a review.
     */
    record ModerateReviewCommand(
            UUID reviewId,
            UUID moderatorId,
            ReviewStatus action,
            String reason) {
    }

    /**
     * Moderates a review (approve, reject, flag).
     */
    ProductReview execute(ModerateReviewCommand command);
}
