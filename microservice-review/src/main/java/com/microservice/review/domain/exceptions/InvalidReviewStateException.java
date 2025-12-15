package com.microservice.review.domain.exceptions;

/**
 * Exception thrown when an invalid review state transition is attempted.
 */
public class InvalidReviewStateException extends DomainException {

    private static final String ERROR_CODE = "INVALID_REVIEW_STATE";

    public InvalidReviewStateException(String message) {
        super(message, ERROR_CODE);
    }
}
