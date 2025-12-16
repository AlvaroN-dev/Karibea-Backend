package com.microservice.review.domain.models;

/**
 * Enum representing the possible statuses of a Product Review.
 * Controls review visibility and moderation state.
 */
public enum ReviewStatus {

    /**
     * Review is pending moderation.
     */
    PENDING,

    /**
     * Review has been approved and is visible.
     */
    APPROVED,

    /**
     * Review has been rejected by moderator.
     */
    REJECTED,

    /**
     * Review has been flagged for additional review.
     */
    FLAGGED,

    /**
     * Review has been soft deleted.
     */
    DELETED
}
