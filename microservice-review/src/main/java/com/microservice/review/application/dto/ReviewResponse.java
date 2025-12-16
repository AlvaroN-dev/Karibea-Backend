package com.microservice.review.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for review information.
 */
@Schema(description = "Review information response")
public class ReviewResponse {

    @Schema(description = "Unique identifier of the review", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID productId;

    @Schema(description = "User profile ID", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID userId;

    @Schema(description = "Order ID", example = "123e4567-e89b-12d3-a456-426614174003")
    private UUID orderId;

    @Schema(description = "Rating (1-5)", example = "5")
    private int rating;

    @Schema(description = "Review title", example = "Great product!")
    private String title;

    @Schema(description = "Review comment", example = "This product exceeded my expectations.")
    private String comment;

    @Schema(description = "Review status", example = "APPROVED")
    private String status;

    @Schema(description = "Number of helpful votes", example = "42")
    private int helpfulVoteCount;

    @Schema(description = "Number of unhelpful votes", example = "3")
    private int unhelpfulVoteCount;

    @Schema(description = "Number of reports", example = "0")
    private int reportedCount;

    @Schema(description = "Whether this is a verified purchase", example = "true")
    private boolean verifiedPurchase;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-15T12:45:00Z")
    private Instant updatedAt;

    @Schema(description = "Review images")
    private List<ReviewImageResponse> images;

    // Constructors
    public ReviewResponse() {
    }

    public ReviewResponse(UUID id, UUID productId, UUID userId, UUID orderId, int rating,
            String title, String comment, String status, int helpfulVoteCount,
            int unhelpfulVoteCount, int reportedCount, boolean verifiedPurchase,
            Instant createdAt, Instant updatedAt, List<ReviewImageResponse> images) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.status = status;
        this.helpfulVoteCount = helpfulVoteCount;
        this.unhelpfulVoteCount = unhelpfulVoteCount;
        this.reportedCount = reportedCount;
        this.verifiedPurchase = verifiedPurchase;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.images = images;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHelpfulVoteCount() {
        return helpfulVoteCount;
    }

    public void setHelpfulVoteCount(int helpfulVoteCount) {
        this.helpfulVoteCount = helpfulVoteCount;
    }

    public int getUnhelpfulVoteCount() {
        return unhelpfulVoteCount;
    }

    public void setUnhelpfulVoteCount(int unhelpfulVoteCount) {
        this.unhelpfulVoteCount = unhelpfulVoteCount;
    }

    public int getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(int reportedCount) {
        this.reportedCount = reportedCount;
    }

    public boolean isVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
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

    public List<ReviewImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ReviewImageResponse> images) {
        this.images = images;
    }
}
