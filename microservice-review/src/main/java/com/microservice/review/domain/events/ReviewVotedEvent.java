package com.microservice.review.domain.events;

import com.microservice.review.domain.models.VoteType;

import java.util.UUID;

/**
 * Event raised when a user votes on a review.
 */
public class ReviewVotedEvent extends DomainEvent {

    private static final String EVENT_TYPE = "REVIEW_VOTED";

    private final UUID reviewId;
    private final UUID userId;
    private final VoteType voteType;

    public ReviewVotedEvent(UUID reviewId, UUID userId, VoteType voteType) {
        super(EVENT_TYPE);
        this.reviewId = reviewId;
        this.userId = userId;
        this.voteType = voteType;
    }

    @Override
    public UUID getAggregateId() {
        return reviewId;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public UUID getUserId() {
        return userId;
    }

    public VoteType getVoteType() {
        return voteType;
    }
}
