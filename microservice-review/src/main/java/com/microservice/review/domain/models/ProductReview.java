package com.microservice.review.domain.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * ProductReview is the Aggregate Root of the Review bounded context.
 * It encapsulates all review-related invariants and business rules.
 * 
 * Invariants protected:
 * - Rating must be between 1 and 5
 * - A user can only review a product once per order
 * - A user can only vote once per review
 * - Only approved reviews are visible to customers
 * - Soft delete pattern for data retention
 */
public class ProductReview {

    private final UUID id;
    private final UUID externalProductId;
    private final UUID externalUserProfileId;
    private final UUID externalOrderId;
    private final UUID externalOrderItemId;
    private Rating rating;
    private String title;
    private String comment;
    private ReviewStatus status;
    private UUID externalModeratorId;
    private Instant moderatedAt;
    private String rejectionReason;
    private int helpfulVoteCount;
    private int unhelpfulVoteCount;
    private int reportedCount;
    private boolean verifiedPurchase;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private boolean deleted;

    private final List<ReviewImage> images;
    private final List<ReviewVote> votes;

    private ProductReview(UUID id, UUID externalProductId, UUID externalUserProfileId,
            UUID externalOrderId, UUID externalOrderItemId, Rating rating,
            String title, String comment, ReviewStatus status,
            UUID externalModeratorId, Instant moderatedAt, String rejectionReason,
            int helpfulVoteCount, int unhelpfulVoteCount, int reportedCount,
            boolean verifiedPurchase, Instant createdAt, Instant updatedAt,
            Instant deletedAt, boolean deleted) {
        this.id = id;
        this.externalProductId = externalProductId;
        this.externalUserProfileId = externalUserProfileId;
        this.externalOrderId = externalOrderId;
        this.externalOrderItemId = externalOrderItemId;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.status = status;
        this.externalModeratorId = externalModeratorId;
        this.moderatedAt = moderatedAt;
        this.rejectionReason = rejectionReason;
        this.helpfulVoteCount = helpfulVoteCount;
        this.unhelpfulVoteCount = unhelpfulVoteCount;
        this.reportedCount = reportedCount;
        this.verifiedPurchase = verifiedPurchase;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.deleted = deleted;
        this.images = new ArrayList<>();
        this.votes = new ArrayList<>();
    }

    /**
     * Factory method to create a new ProductReview.
     */
    public static ProductReview create(UUID externalProductId, UUID externalUserProfileId,
            UUID externalOrderId, UUID externalOrderItemId,
            int ratingValue, String title, String comment,
            boolean verifiedPurchase) {
        validateRequired(externalProductId, "externalProductId");
        validateRequired(externalUserProfileId, "externalUserProfileId");
        validateRequired(comment, "comment");

        return new ProductReview(
                UUID.randomUUID(),
                externalProductId,
                externalUserProfileId,
                externalOrderId,
                externalOrderItemId,
                Rating.of(ratingValue),
                title != null ? title.trim() : null,
                comment.trim(),
                ReviewStatus.PENDING,
                null,
                null,
                null,
                0,
                0,
                0,
                verifiedPurchase,
                Instant.now(),
                Instant.now(),
                null,
                false);
    }

    /**
     * Reconstitutes a ProductReview from persistence.
     */
    public static ProductReview reconstitute(UUID id, UUID externalProductId, UUID externalUserProfileId,
            UUID externalOrderId, UUID externalOrderItemId,
            int ratingValue, String title, String comment,
            ReviewStatus status, UUID externalModeratorId,
            Instant moderatedAt, String rejectionReason,
            int helpfulVoteCount, int unhelpfulVoteCount,
            int reportedCount, boolean verifiedPurchase,
            Instant createdAt, Instant updatedAt,
            Instant deletedAt, boolean deleted) {
        return new ProductReview(id, externalProductId, externalUserProfileId, externalOrderId,
                externalOrderItemId, Rating.of(ratingValue), title, comment, status,
                externalModeratorId, moderatedAt, rejectionReason, helpfulVoteCount,
                unhelpfulVoteCount, reportedCount, verifiedPurchase, createdAt, updatedAt,
                deletedAt, deleted);
    }

    // ========== Business Operations ==========

    /**
     * Approves the review, making it visible to customers.
     */
    public void approve(UUID moderatorId) {
        validateRequired(moderatorId, "moderatorId");
        if (this.deleted) {
            throw new IllegalStateException("Cannot approve a deleted review.");
        }
        this.status = ReviewStatus.APPROVED;
        this.externalModeratorId = moderatorId;
        this.moderatedAt = Instant.now();
        this.rejectionReason = null;
        this.updatedAt = Instant.now();
    }

    /**
     * Rejects the review with a reason.
     */
    public void reject(UUID moderatorId, String reason) {
        validateRequired(moderatorId, "moderatorId");
        validateRequired(reason, "reason");
        if (this.deleted) {
            throw new IllegalStateException("Cannot reject a deleted review.");
        }
        this.status = ReviewStatus.REJECTED;
        this.externalModeratorId = moderatorId;
        this.moderatedAt = Instant.now();
        this.rejectionReason = reason.trim();
        this.updatedAt = Instant.now();
    }

    /**
     * Flags the review for additional review.
     */
    public void flag(UUID moderatorId, String reason) {
        validateRequired(moderatorId, "moderatorId");
        if (this.deleted) {
            throw new IllegalStateException("Cannot flag a deleted review.");
        }
        this.status = ReviewStatus.FLAGGED;
        this.externalModeratorId = moderatorId;
        this.moderatedAt = Instant.now();
        this.rejectionReason = reason;
        this.updatedAt = Instant.now();
    }

    /**
     * Soft deletes the review.
     */
    public void softDelete() {
        this.status = ReviewStatus.DELETED;
        this.deleted = true;
        this.deletedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Records a vote on this review.
     * Enforces invariant: one vote per user.
     */
    public void addVote(UUID userId, VoteType voteType) {
        validateRequired(userId, "userId");
        validateRequired(voteType, "voteType");

        if (this.deleted) {
            throw new IllegalStateException("Cannot vote on a deleted review.");
        }

        // Check if user has already voted
        boolean hasVoted = this.votes.stream()
                .anyMatch(v -> v.getExternalUserProfileId().equals(userId));

        if (hasVoted) {
            throw new IllegalStateException("User has already voted on this review.");
        }

        ReviewVote vote = ReviewVote.create(this.id, userId, voteType);
        this.votes.add(vote);

        // Update vote counts
        if (VoteType.HELPFUL.equals(voteType)) {
            this.helpfulVoteCount++;
        } else {
            this.unhelpfulVoteCount++;
        }

        this.updatedAt = Instant.now();
    }

    /**
     * Increments the reported count.
     */
    public void report() {
        if (this.deleted) {
            throw new IllegalStateException("Cannot report a deleted review.");
        }
        this.reportedCount++;
        this.updatedAt = Instant.now();
    }

    /**
     * Adds an image to this review.
     */
    public void addImage(ReviewImage image) {
        validateRequired(image, "image");
        this.images.add(image);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes an image from this review.
     */
    public void removeImage(UUID imageId) {
        validateRequired(imageId, "imageId");
        boolean removed = this.images.removeIf(img -> img.getId().equals(imageId));
        if (removed) {
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Updates the review content.
     * Only allowed for pending reviews.
     */
    public void updateContent(int ratingValue, String title, String comment) {
        if (this.status != ReviewStatus.PENDING) {
            throw new IllegalStateException("Cannot update a review that is not pending.");
        }
        this.rating = Rating.of(ratingValue);
        this.title = title != null ? title.trim() : null;
        if (comment != null && !comment.isBlank()) {
            this.comment = comment.trim();
        }
        this.updatedAt = Instant.now();
    }

    // ========== Validation Helpers ==========

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be null.");
        }
        if (value instanceof String && ((String) value).isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be blank.");
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public UUID getExternalProductId() {
        return externalProductId;
    }

    public UUID getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public UUID getExternalOrderId() {
        return externalOrderId;
    }

    public UUID getExternalOrderItemId() {
        return externalOrderItemId;
    }

    public Rating getRating() {
        return rating;
    }

    public int getRatingValue() {
        return rating.getValue();
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public UUID getExternalModeratorId() {
        return externalModeratorId;
    }

    public Instant getModeratedAt() {
        return moderatedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public int getHelpfulVoteCount() {
        return helpfulVoteCount;
    }

    public int getUnhelpfulVoteCount() {
        return unhelpfulVoteCount;
    }

    public int getReportedCount() {
        return reportedCount;
    }

    public boolean isVerifiedPurchase() {
        return verifiedPurchase;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<ReviewImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public List<ReviewVote> getVotes() {
        return Collections.unmodifiableList(votes);
    }

    /**
     * Adds an image during reconstitution (used by repository).
     */
    public void addImageForReconstitution(ReviewImage image) {
        this.images.add(image);
    }

    /**
     * Adds a vote during reconstitution (used by repository).
     */
    public void addVoteForReconstitution(ReviewVote vote) {
        this.votes.add(vote);
    }
}
