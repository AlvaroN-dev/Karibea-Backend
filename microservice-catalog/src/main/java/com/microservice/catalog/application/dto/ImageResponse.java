package com.microservice.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for image information.
 */
@Schema(description = "Image information response")
public class ImageResponse {

    @Schema(description = "Unique identifier of the image", example = "123e4567-e89b-12d3-a456-426614174003")
    private UUID id;

    @Schema(description = "Image URL", example = "https://cdn.example.com/images/product.jpg")
    private String url;

    @Schema(description = "Display order", example = "1")
    private Integer displayOrder;

    @Schema(description = "Primary image flag", example = "true")
    private boolean primary;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    // Constructors
    public ImageResponse() {
    }

    public ImageResponse(UUID id, String url, Integer displayOrder, boolean primary, Instant createdAt) {
        this.id = id;
        this.url = url;
        this.displayOrder = displayOrder;
        this.primary = primary;
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

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
