package com.microservice.inventory.application.dto;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to initialize stock for a product variant in a warehouse")
public class CreateStockRequest {

        @Schema(description = "Unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Product ID is required")
        private UUID externalProductId;

        @Schema(description = "Unique identifier of the product variant", example = "123e4567-e89b-12d3-a456-426614174001", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Variant ID is required")
        private UUID externalVariantId;

        @Schema(description = "Unique identifier of the warehouse", example = "123e4567-e89b-12d3-a456-426614174002", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Warehouse ID is required")
        private UUID warehouseId;

        @Schema(description = "Initial quantity of stock available", example = "100", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 0, message = "Initial quantity cannot be negative")
        private int initialQuantity;

        @Schema(description = "Threshold at which the stock is considered low", example = "10", minimum = "0")
        @Min(value = 0, message = "Low stock threshold cannot be negative")
        private int lowStockThreshold;

        @Schema(description = "Quantity to reorder when stock reaches the threshold", example = "50", minimum = "0")
        @Min(value = 0, message = "Reorder point cannot be negative")
        private int reorderPoint;

        // Constructors
        public CreateStockRequest() {
        }

        public CreateStockRequest(UUID externalProductId, UUID externalVariantId, UUID warehouseId, int initialQuantity,
                        int lowStockThreshold, int reorderPoint) {
                this.externalProductId = externalProductId;
                this.externalVariantId = externalVariantId;
                this.warehouseId = warehouseId;
                this.initialQuantity = initialQuantity;
                this.lowStockThreshold = lowStockThreshold;
                this.reorderPoint = reorderPoint;
        }

        // Getters and Setters
        public UUID getExternalProductId() {
                return externalProductId;
        }

        public void setExternalProductId(UUID externalProductId) {
                this.externalProductId = externalProductId;
        }

        public UUID getExternalVariantId() {
                return externalVariantId;
        }

        public void setExternalVariantId(UUID externalVariantId) {
                this.externalVariantId = externalVariantId;
        }

        public UUID getWarehouseId() {
                return warehouseId;
        }

        public void setWarehouseId(UUID warehouseId) {
                this.warehouseId = warehouseId;
        }

        public int getInitialQuantity() {
                return initialQuantity;
        }

        public void setInitialQuantity(int initialQuantity) {
                this.initialQuantity = initialQuantity;
        }

        public int getLowStockThreshold() {
                return lowStockThreshold;
        }

        public void setLowStockThreshold(int lowStockThreshold) {
                this.lowStockThreshold = lowStockThreshold;
        }

        public int getReorderPoint() {
                return reorderPoint;
        }

        public void setReorderPoint(int reorderPoint) {
                this.reorderPoint = reorderPoint;
        }
}
