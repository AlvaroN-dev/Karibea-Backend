package com.microservice.order.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * Request DTO for cancelling an order.
 */
@Builder
public record CancelOrderRequest(
        @NotNull(message = "Order ID is required") UUID orderId,

        @NotBlank(message = "Reason is required") String reason) {
}
