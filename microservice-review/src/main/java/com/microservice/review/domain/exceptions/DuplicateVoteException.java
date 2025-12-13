package com.microservice.review.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a user has already voted on a review.
 */
public class DuplicateVoteException extends DomainException {

    private static final String ERROR_CODE = "DUPLICATE_VOTE";

    public DuplicateVoteException(UUID userId, UUID reviewId) {
        super("User " + userId + " has already voted on review " + reviewId, ERROR_CODE);
    }
}
