package com.microservice.review.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a vote on a review.
 * Tracks whether a user found a review helpful or not.
 */
public class ReviewVote {

    private final UUID id;
    private final UUID reviewId;
    private final UUID externalUserProfileId;
    private final VoteType voteType;
    private final Instant createdAt;

    private ReviewVote(UUID id, UUID reviewId, UUID externalUserProfileId,
            VoteType voteType, Instant createdAt) {
        this.id = id;
        this.reviewId = reviewId;
        this.externalUserProfileId = externalUserProfileId;
        this.voteType = voteType;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create a new ReviewVote.
     */
    public static ReviewVote create(UUID reviewId, UUID externalUserProfileId, VoteType voteType) {
        validateRequired(reviewId, "reviewId");
        validateRequired(externalUserProfileId, "externalUserProfileId");
        validateRequired(voteType, "voteType");

        return new ReviewVote(
                UUID.randomUUID(),
                reviewId,
                externalUserProfileId,
                voteType,
                Instant.now());
    }

    /**
     * Reconstitutes a ReviewVote from persistence.
     */
    public static ReviewVote reconstitute(UUID id, UUID reviewId, UUID externalUserProfileId,
            VoteType voteType, Instant createdAt) {
        return new ReviewVote(id, reviewId, externalUserProfileId, voteType, createdAt);
    }

    // ========== Validation Helpers ==========

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be null.");
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public UUID getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Checks if this vote is a helpful vote.
     */
    public boolean isHelpful() {
        return VoteType.HELPFUL.equals(voteType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ReviewVote that = (ReviewVote) o;
        return reviewId.equals(that.reviewId) &&
                externalUserProfileId.equals(that.externalUserProfileId);
    }

    @Override
    public int hashCode() {
        return 31 * reviewId.hashCode() + externalUserProfileId.hashCode();
    }
}
