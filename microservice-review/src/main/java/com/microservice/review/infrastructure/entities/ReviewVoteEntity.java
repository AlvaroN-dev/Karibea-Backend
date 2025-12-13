package com.microservice.review.infrastructure.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA Entity for ReviewVote.
 */
@Entity
@Table(name = "review_votes")
public class ReviewVoteEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ProductReviewEntity review;

    @Column(name = "external_user_profiles_id", nullable = false)
    private UUID externalUserProfileId;

    @Column(name = "vote_type", nullable = false, length = 50)
    private String voteType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Constructors
    public ReviewVoteEntity() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProductReviewEntity getReview() {
        return review;
    }

    public void setReview(ProductReviewEntity review) {
        this.review = review;
    }

    public UUID getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public void setExternalUserProfileId(UUID externalUserProfileId) {
        this.externalUserProfileId = externalUserProfileId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
