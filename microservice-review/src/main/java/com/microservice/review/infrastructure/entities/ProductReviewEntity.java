package com.microservice.review.infrastructure.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for ProductReview.
 */
@Entity
@Table(name = "products_review")
public class ProductReviewEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "external_product_id", nullable = false)
    private UUID externalProductId;

    @Column(name = "external_user_profiles_id", nullable = false)
    private UUID externalUserProfileId;

    @Column(name = "external_order_id")
    private UUID externalOrderId;

    @Column(name = "external_order_item_id")
    private UUID externalOrderItemId;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "statu_review_id")
    private UUID statusId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ReviewStatusEntity status;

    @Column(name = "externar_moderated_id")
    private UUID externalModeratorId;

    @Column(name = "moderated_at")
    private Instant moderatedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "helpful_vote", nullable = false)
    private Integer helpfulVoteCount = 0;

    @Column(name = "unhelpful_vote", nullable = false)
    private Integer unhelpfulVoteCount = 0;

    @Column(name = "reported_count", nullable = false)
    private Integer reportedCount = 0;

    @Column(name = "is_verified_purchase", nullable = false)
    private Boolean verifiedPurchase = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewVoteEntity> votes = new ArrayList<>();

    // Constructors
    public ProductReviewEntity() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Helper methods
    public void addImage(ReviewImageEntity image) {
        images.add(image);
        image.setReview(this);
    }

    public void addVote(ReviewVoteEntity vote) {
        votes.add(vote);
        vote.setReview(this);
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(UUID externalProductId) {
        this.externalProductId = externalProductId;
    }

    public UUID getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public void setExternalUserProfileId(UUID externalUserProfileId) {
        this.externalUserProfileId = externalUserProfileId;
    }

    public UUID getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(UUID externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public UUID getExternalOrderItemId() {
        return externalOrderItemId;
    }

    public void setExternalOrderItemId(UUID externalOrderItemId) {
        this.externalOrderItemId = externalOrderItemId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getStatusId() {
        return statusId;
    }

    public void setStatusId(UUID statusId) {
        this.statusId = statusId;
    }

    public ReviewStatusEntity getStatus() {
        return status;
    }

    public void setStatus(ReviewStatusEntity status) {
        this.status = status;
    }

    public UUID getExternalModeratorId() {
        return externalModeratorId;
    }

    public void setExternalModeratorId(UUID externalModeratorId) {
        this.externalModeratorId = externalModeratorId;
    }

    public Instant getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(Instant moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Integer getHelpfulVoteCount() {
        return helpfulVoteCount;
    }

    public void setHelpfulVoteCount(Integer helpfulVoteCount) {
        this.helpfulVoteCount = helpfulVoteCount;
    }

    public Integer getUnhelpfulVoteCount() {
        return unhelpfulVoteCount;
    }

    public void setUnhelpfulVoteCount(Integer unhelpfulVoteCount) {
        this.unhelpfulVoteCount = unhelpfulVoteCount;
    }

    public Integer getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(Integer reportedCount) {
        this.reportedCount = reportedCount;
    }

    public Boolean getVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<ReviewImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ReviewImageEntity> images) {
        this.images = images;
    }

    public List<ReviewVoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(List<ReviewVoteEntity> votes) {
        this.votes = votes;
    }
}
