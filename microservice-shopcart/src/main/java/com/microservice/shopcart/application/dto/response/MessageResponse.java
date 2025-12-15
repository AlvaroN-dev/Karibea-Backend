package com.microservice.shopcart.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic message response for operations.
 */
@Schema(description = "Generic operation response message")
public class MessageResponse {

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Operation success status", example = "true")
    private boolean success;

    // Constructors
    public MessageResponse() {
    }

    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
