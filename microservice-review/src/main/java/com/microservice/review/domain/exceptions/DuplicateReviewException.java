package com.microservice.review.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a user has already reviewed a product.
 */
public class DuplicateReviewException extends DomainException {

    private static final String ERROR_CODE = "DUPLICATE_REVIEW";

    public DuplicateReviewException(UUID userId, UUID productId) {
        super("User " + userId + " has already reviewed product " + productId, ERROR_CODE);
    }
}
