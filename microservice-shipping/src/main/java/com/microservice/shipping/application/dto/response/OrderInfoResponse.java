package com.microservice.shipping.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO containing enriched order information.
 * Represents external order data referenced by the shipment.
 */
@Schema(description = "Order information associated with the shipment")
public class OrderInfoResponse {

    @Schema(
            description = "Unique identifier of the order",
            example = "550e8400-e29b-41d4-a716-446655440001"
    )
    private UUID id;

    @Schema(
            description = "Human-readable order number",
            example = "ORD-20241215-A1B2C3D4"
    )
    private String orderNumber;

    @Schema(
            description = "Current status of the order",
            example = "CONFIRMED"
    )
    private String orderStatus;

    @Schema(
            description = "Total amount of the order",
            example = "199.99"
    )
    private String grandTotal;

    @Schema(
            description = "Currency of the order",
            example = "USD"
    )
    private String currency;

    // Constructors
    public OrderInfoResponse() {
    }

    public OrderInfoResponse(UUID id, String orderNumber, String orderStatus, 
                              String grandTotal, String currency) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.grandTotal = grandTotal;
        this.currency = currency;
    }

    // Static factory for creating with just ID (when external data not available)
    public static OrderInfoResponse withId(UUID id) {
        OrderInfoResponse response = new OrderInfoResponse();
        response.setId(id);
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
