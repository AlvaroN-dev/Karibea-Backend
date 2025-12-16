package com.microservice.review.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for moderating a review.
 */
@Schema(description = "Request payload for moderating a review")
public class ModerateReviewRequest {

    @Schema(description = "Moderator user ID", example = "123e4567-e89b-12d3-a456-426614174004", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Moderator ID is required")
    private UUID moderatorId;

    @Schema(description = "Moderation action", example = "APPROVED", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {
            "APPROVED", "REJECTED", "FLAGGED" })
    @NotBlank(message = "Action is required")
    private String action;

    @Schema(description = "Reason for rejection or flagging", example = "Contains inappropriate content")
    private String reason;

    // Constructors
    public ModerateReviewRequest() {
    }

    public ModerateReviewRequest(UUID moderatorId, String action, String reason) {
        this.moderatorId = moderatorId;
        this.action = action;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
