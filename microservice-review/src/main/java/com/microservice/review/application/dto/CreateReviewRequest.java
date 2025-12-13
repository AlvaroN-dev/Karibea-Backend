package com.microservice.review.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new review.
 */
@Schema(description = "Request payload for creating a new product review")
public class CreateReviewRequest {

    @Schema(description = "Product ID to review", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Schema(description = "User profile ID", example = "123e4567-e89b-12d3-a456-426614174001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "User ID is required")
    private UUID userId;

    @Schema(description = "Order ID associated with this review", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID orderId;

    @Schema(description = "Order item ID associated with this review", example = "123e4567-e89b-12d3-a456-426614174003")
    private UUID orderItemId;

    @Schema(description = "Rating from 1 to 5 stars", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "5")
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Schema(description = "Review title", example = "Great product!", maxLength = 255)
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Schema(description = "Review comment", example = "This product exceeded my expectations. The quality is amazing!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Comment is required")
    @Size(min = 10, max = 5000, message = "Comment must be between 10 and 5000 characters")
    private String comment;

    @Schema(description = "List of image URLs for the review")
    private List<String> imageUrls;

    @Schema(description = "Whether this is a verified purchase", example = "true")
    private boolean verifiedPurchase;

    // Constructors
    public CreateReviewRequest() {
    }

    public CreateReviewRequest(UUID productId, UUID userId, UUID orderId, UUID orderItemId,
            Integer rating, String title, String comment,
            List<String> imageUrls, boolean verifiedPurchase) {
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.imageUrls = imageUrls;
        this.verifiedPurchase = verifiedPurchase;
    }

    // Getters and Setters
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

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(UUID orderItemId) {
        this.orderItemId = orderItemId;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public boolean isVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }
}
