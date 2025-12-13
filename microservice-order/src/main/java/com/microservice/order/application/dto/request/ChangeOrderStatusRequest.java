package com.microservice.order.application.dto.request;

import com.microservice.order.domain.models.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * Request DTO for changing order status.
 */
@Builder
public record ChangeOrderStatusRequest(
        @NotNull(message = "Order ID is required") UUID orderId,

        @NotNull(message = "New status is required") OrderStatusEnum newStatus,

        String reason,

        UUID relatedEntityId) {
}
