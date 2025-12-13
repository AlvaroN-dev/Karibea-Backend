package com.microservice.review.domain.port.in;

import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.VoteType;

import java.util.UUID;

/**
 * Input port for voting on a review.
 */
public interface VoteReviewUseCase {

    /**
     * Command object for voting on a review.
     */
    record VoteReviewCommand(
            UUID reviewId,
            UUID userId,
            VoteType voteType) {
    }

    /**
     * Votes on a review (helpful or not helpful).
     */
    ProductReview execute(VoteReviewCommand command);
}
