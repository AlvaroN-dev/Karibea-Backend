package com.microservice.review.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing an image attached to a review.
 * Part of the ProductReview aggregate.
 */
public class ReviewImage {

    private final UUID id;
    private final UUID reviewId;
    private String url;
    private Integer displayOrder;
    private Instant createdAt;

    private ReviewImage(UUID id, UUID reviewId, String url, Integer displayOrder, Instant createdAt) {
        this.id = id;
        this.reviewId = reviewId;
        this.url = url;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create a new ReviewImage.
     */
    public static ReviewImage create(UUID reviewId, String url, Integer displayOrder) {
        validateRequired(reviewId, "reviewId");
        validateRequired(url, "url");

        return new ReviewImage(
                UUID.randomUUID(),
                reviewId,
                url.trim(),
                displayOrder != null ? displayOrder : 0,
                Instant.now());
    }

    /**
     * Reconstitutes a ReviewImage from persistence.
     */
    public static ReviewImage reconstitute(UUID id, UUID reviewId, String url,
            Integer displayOrder, Instant createdAt) {
        return new ReviewImage(id, reviewId, url, displayOrder, createdAt);
    }

    // ========== Business Operations ==========

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public void updateUrl(String url) {
        validateRequired(url, "url");
        this.url = url.trim();
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

    public UUID getReviewId() {
        return reviewId;
    }

    public String getUrl() {
        return url;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
