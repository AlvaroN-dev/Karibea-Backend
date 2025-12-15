package com.microservice.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for cancelling an order.
 * Contains the order ID and reason for cancellation.
 */
@Schema(description = "Request to cancel an existing order")
public class CancelOrderRequest {

    @Schema(description = "Unique identifier of the order to cancel", example = "550e8400-e29b-41d4-a716-446655440100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @Schema(description = "Reason for cancelling the order", example = "Customer requested cancellation", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Reason is required")
    private String reason;

    // Constructors
    public CancelOrderRequest() {
    }

    public CancelOrderRequest(UUID orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
