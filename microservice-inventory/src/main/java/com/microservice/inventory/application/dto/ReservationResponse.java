package com.microservice.inventory.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import com.microservice.inventory.domain.models.enums.ReservationStatus;
import com.microservice.inventory.domain.models.enums.ReservationType;

@Schema(description = "Response containing reservation details")
public class ReservationResponse {

        @Schema(description = "Unique identifier of the reservation", example = "123e4567-e89b-12d3-a456-426614174555")
        private UUID id;

        @Schema(description = "Unique identifier of the stock record", example = "123e4567-e89b-12d3-a456-426614174099")
        private UUID stockId;

        @Schema(description = "External order ID associated with reservation", example = "123e4567-e89b-12d3-a456-426614174777")
        private UUID externalOrderId;

        @Schema(description = "External cart ID associated with reservation", example = "123e4567-e89b-12d3-a456-426614174888")
        private UUID externalCartId;

        @Schema(description = "Reserved quantity", example = "5")
        private int quantity;

        @Schema(description = "Type of reservation", example = "ORDER")
        private ReservationType reservationType;

        @Schema(description = "Current status of the reservation", example = "PENDING")
        private ReservationStatus status;

        @Schema(description = "Expiration timestamp", example = "2023-12-31T23:59:59")
        private LocalDateTime expiresAt;

        @Schema(description = "Creation timestamp", example = "2023-10-27T10:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "Update timestamp", example = "2023-10-27T10:00:00")
        private LocalDateTime updatedAt;

        // Constructors
        public ReservationResponse() {
        }

        public ReservationResponse(UUID id, UUID stockId, UUID externalOrderId, UUID externalCartId, int quantity,
                        ReservationType reservationType, ReservationStatus status, LocalDateTime expiresAt,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
                this.id = id;
                this.stockId = stockId;
                this.externalOrderId = externalOrderId;
                this.externalCartId = externalCartId;
                this.quantity = quantity;
                this.reservationType = reservationType;
                this.status = status;
                this.expiresAt = expiresAt;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
        }

        // Getters and Setters
        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public UUID getStockId() {
                return stockId;
        }

        public void setStockId(UUID stockId) {
                this.stockId = stockId;
        }

        public UUID getExternalOrderId() {
                return externalOrderId;
        }

        public void setExternalOrderId(UUID externalOrderId) {
                this.externalOrderId = externalOrderId;
        }

        public UUID getExternalCartId() {
                return externalCartId;
        }

        public void setExternalCartId(UUID externalCartId) {
                this.externalCartId = externalCartId;
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

        public ReservationStatus getStatus() {
                return status;
        }

        public void setStatus(ReservationStatus status) {
                this.status = status;
        }

        public LocalDateTime getExpiresAt() {
                return expiresAt;
        }

        public void setExpiresAt(LocalDateTime expiresAt) {
                this.expiresAt = expiresAt;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
        }
}
