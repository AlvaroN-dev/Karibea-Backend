package com.microservice.review.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for voting on a review.
 */
@Schema(description = "Request payload for voting on a review")
public class VoteReviewRequest {

    @Schema(description = "User ID voting on the review", example = "123e4567-e89b-12d3-a456-426614174005", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "User ID is required")
    private UUID userId;

    @Schema(description = "Vote type", example = "HELPFUL", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {
            "HELPFUL", "NOT_HELPFUL" })
    @NotBlank(message = "Vote type is required")
    private String voteType;

    // Constructors
    public VoteReviewRequest() {
    }

    public VoteReviewRequest(UUID userId, String voteType) {
        this.userId = userId;
        this.voteType = voteType;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
}
