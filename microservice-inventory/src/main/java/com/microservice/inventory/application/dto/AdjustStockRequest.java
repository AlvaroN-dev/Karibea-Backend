package com.microservice.inventory.application.dto;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import com.microservice.inventory.domain.models.enums.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to adjust stock quantity manually")
public class AdjustStockRequest {

        @Schema(description = "Unique identifier of the stock record", example = "123e4567-e89b-12d3-a456-426614174099", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Stock ID is required")
        private UUID stockId;

        @Schema(description = "Type of movement (e.g., PURCHASE, SALE, ADJUSTMENT_IN)", example = "ADJUSTMENT_IN", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Movement type is required")
        private MovementType movementType;

        @Schema(description = "Quantity to adjust (must be positive)", example = "10", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;

        @Schema(description = "Type of reference document (e.g., ORDER, PO)", example = "PURCHASE_ORDER")
        private String referenceType;

        @Schema(description = "External ID of the reference document", example = "123e4567-e89b-12d3-a456-426614174999")
        private UUID externalReferenceId;

        @Schema(description = "Optional note describing the adjustment", example = "Stock correction from audit")
        private String note;

        // Constructors
        public AdjustStockRequest() {
        }

        public AdjustStockRequest(UUID stockId, MovementType movementType, int quantity, String referenceType,
                        UUID externalReferenceId, String note) {
                this.stockId = stockId;
                this.movementType = movementType;
                this.quantity = quantity;
                this.referenceType = referenceType;
                this.externalReferenceId = externalReferenceId;
                this.note = note;
        }

        // Getters and Setters
        public UUID getStockId() {
                return stockId;
        }

        public void setStockId(UUID stockId) {
                this.stockId = stockId;
        }

        public MovementType getMovementType() {
                return movementType;
        }

        public void setMovementType(MovementType movementType) {
                this.movementType = movementType;
        }

        public int getQuantity() {
                return quantity;
        }

        public void setQuantity(int quantity) {
                this.quantity = quantity;
        }

        public String getReferenceType() {
                return referenceType;
        }

        public void setReferenceType(String referenceType) {
                this.referenceType = referenceType;
        }

        public UUID getExternalReferenceId() {
                return externalReferenceId;
        }

        public void setExternalReferenceId(UUID externalReferenceId) {
                this.externalReferenceId = externalReferenceId;
        }

        public String getNote() {
                return note;
        }

        public void setNote(String note) {
                this.note = note;
        }
}
