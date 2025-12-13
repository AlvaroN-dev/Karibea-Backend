package com.microservice.review.domain.events;

import java.util.UUID;

/**
 * Event raised when a review is deleted (soft delete).
 */
public class ReviewDeletedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "REVIEW_DELETED";

    private final UUID reviewId;
    private final UUID productId;

    public ReviewDeletedEvent(UUID reviewId, UUID productId) {
        super(EVENT_TYPE);
        this.reviewId = reviewId;
        this.productId = productId;
    }

    @Override
    public UUID getAggregateId() {
        return reviewId;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public UUID getProductId() {
        return productId;
    }
}
