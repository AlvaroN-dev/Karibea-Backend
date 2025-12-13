package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;

import java.util.List;
import java.util.UUID;

/**
 * Input port for creating a new review.
 * Follows ISP - small, focused interface.
 */
public interface CreateReviewUseCase {

    /**
     * Command object for creating a review.
     */
    record CreateReviewCommand(
            UUID productId,
            UUID userId,
            UUID orderId,
            UUID orderItemId,
            int rating,
            String title,
            String comment,
            List<String> imageUrls,
            boolean verifiedPurchase) {
    }

    /**
     * Creates a new review.
     */
    ProductReview execute(CreateReviewCommand command);
}
