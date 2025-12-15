package com.microservice.inventory.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import com.microservice.inventory.domain.models.enums.ReservationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to reserve stock for an order or cart")
public class ReserveStockRequest {

        @Schema(description = "Unique identifier of the stock record", example = "123e4567-e89b-12d3-a456-426614174099", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Stock ID is required")
        private UUID stockId;

        @Schema(description = "Quantity to reserve", example = "5", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;

        @Schema(description = "Type of reservation (e.g., ORDER, CART)", example = "ORDER", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Reservation type is required")
        private ReservationType reservationType;

        @Schema(description = "External ID of the cart (if applicable)", example = "123e4567-e89b-12d3-a456-426614174888")
        private UUID externalCartId;

        @Schema(description = "External ID of the order (if applicable)", example = "123e4567-e89b-12d3-a456-426614174777")
        private UUID externalOrderId;

        @Schema(description = "Expiration time for the reservation", example = "2023-12-31T23:59:59")
        private LocalDateTime expiresAt;

        // Constructors
        public ReserveStockRequest() {
        }

        public ReserveStockRequest(UUID stockId, int quantity, ReservationType reservationType, UUID externalCartId,
                        UUID externalOrderId, LocalDateTime expiresAt) {
                this.stockId = stockId;
                this.quantity = quantity;
                this.reservationType = reservationType;
                this.externalCartId = externalCartId;
                this.externalOrderId = externalOrderId;
                this.expiresAt = expiresAt;
        }

        // Getters and Setters
        public UUID getStockId() {
                return stockId;
        }

        public void setStockId(UUID stockId) {
                this.stockId = stockId;
        }

        public int getQuantity() {
                return quantity;
        }

        public void setQuantity(int quantity) {
                this.quantity = quantity;
        }

        public ReservationType getReservationType() {
                return reservationType;
        }

        public void setReservationType(ReservationType reservationType) {
                this.reservationType = reservationType;
        }

        public UUID getExternalCartId() {
                return externalCartId;
        }

        public void setExternalCartId(UUID externalCartId) {
                this.externalCartId = externalCartId;
        }

        public UUID getExternalOrderId() {
                return externalOrderId;
        }

        public void setExternalOrderId(UUID externalOrderId) {
                this.externalOrderId = externalOrderId;
        }

        public LocalDateTime getExpiresAt() {
                return expiresAt;
        }

        public void setExpiresAt(LocalDateTime expiresAt) {
                this.expiresAt = expiresAt;
        }
}
