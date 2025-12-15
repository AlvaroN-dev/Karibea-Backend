package com.microservice.review.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for review image information.
 */
@Schema(description = "Review image information")
public class ReviewImageResponse {

    @Schema(description = "Image ID", example = "123e4567-e89b-12d3-a456-426614174010")
    private UUID id;

    @Schema(description = "Image URL", example = "https://cdn.example.com/reviews/image1.jpg")
    private String url;

    @Schema(description = "Display order", example = "1")
    private Integer displayOrder;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    // Constructors
    public ReviewImageResponse() {
    }

    public ReviewImageResponse(UUID id, String url, Integer displayOrder, Instant createdAt) {
        this.id = id;
        this.url = url;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
