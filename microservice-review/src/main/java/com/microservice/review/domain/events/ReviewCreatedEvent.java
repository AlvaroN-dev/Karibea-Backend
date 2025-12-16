package com.microservice.review.domain.events;

import java.util.UUID;

/**
 * Event raised when a new review is created.
 */
public class ReviewCreatedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "REVIEW_CREATED";

    private final UUID reviewId;
    private final UUID productId;
    private final UUID userId;
    private final int rating;

    public ReviewCreatedEvent(UUID reviewId, UUID productId, UUID userId, int rating) {
        super(EVENT_TYPE);
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
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

    public UUID getUserId() {
        return userId;
    }

    public int getRating() {
        return rating;
    }
}
