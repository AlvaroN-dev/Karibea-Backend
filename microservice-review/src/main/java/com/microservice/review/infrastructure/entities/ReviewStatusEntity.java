package com.microservice.review.infrastructure.entities;

/**
 * JPA enum for review status.
 */
public enum ReviewStatusEntity {
    PENDING,
    APPROVED,
    REJECTED,
    FLAGGED,
    DELETED
}
