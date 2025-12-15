package com.microservice.review.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a review is not found.
 */
public class ReviewNotFoundException extends DomainException {

    private static final String ERROR_CODE = "REVIEW_NOT_FOUND";

    public ReviewNotFoundException(UUID reviewId) {
        super("Review not found with ID: " + reviewId, ERROR_CODE);
    }
}
