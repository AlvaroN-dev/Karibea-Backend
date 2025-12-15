package com.microservice.review.domain.events;

import com.microservice.review.domain.models.ReviewStatus;

import java.util.UUID;

/**
 * Event raised when a review is moderated (approved, rejected, flagged).
 */
public class ReviewModeratedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "REVIEW_MODERATED";

    private final UUID reviewId;
    private final UUID productId;
    private final UUID moderatorId;
    private final ReviewStatus newStatus;
    private final String reason;

    public ReviewModeratedEvent(UUID reviewId, UUID productId, UUID moderatorId,
            ReviewStatus newStatus, String reason) {
        super(EVENT_TYPE);
        this.reviewId = reviewId;
        this.productId = productId;
        this.moderatorId = moderatorId;
        this.newStatus = newStatus;
        this.reason = reason;
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

    public UUID getModeratorId() {
        return moderatorId;
    }

    public ReviewStatus getNewStatus() {
        return newStatus;
    }

    public String getReason() {
        return reason;
    }
}
