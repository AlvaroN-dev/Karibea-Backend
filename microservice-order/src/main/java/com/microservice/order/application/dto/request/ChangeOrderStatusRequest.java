package com.microservice.order.application.dto.request;

import com.microservice.order.domain.models.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for changing order status.
 * Contains the order ID, new status, optional reason and related entity ID.
 */
@Schema(description = "Request to change the status of an existing order")
public class ChangeOrderStatusRequest {

    @Schema(description = "Unique identifier of the order", example = "550e8400-e29b-41d4-a716-446655440100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @Schema(description = "New status to be applied to the order", example = "CONFIRMED", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "New status is required")
    private OrderStatusEnum newStatus;

    @Schema(description = "Optional reason for the status change", example = "Payment confirmed by payment service")
    private String reason;

    @Schema(description = "Related entity ID (e.g., payment ID, shipment ID)", example = "660e8400-e29b-41d4-a716-446655440200")
    private UUID relatedEntityId;

    // Constructors
    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(UUID orderId, OrderStatusEnum newStatus, String reason, UUID relatedEntityId) {
        this.orderId = orderId;
        this.newStatus = newStatus;
        this.reason = reason;
        this.relatedEntityId = relatedEntityId;
    }

    // Getters and Setters
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public OrderStatusEnum getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatusEnum newStatus) {
        this.newStatus = newStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(UUID relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
}
