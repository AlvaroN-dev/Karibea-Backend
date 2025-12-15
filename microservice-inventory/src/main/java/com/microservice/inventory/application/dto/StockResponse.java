package com.microservice.inventory.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing stock details")
public class StockResponse {

        @Schema(description = "Unique identifier of the stock record", example = "123e4567-e89b-12d3-a456-426614174099")
        private UUID id;

        @Schema(description = "Unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID externalProductId;

        @Schema(description = "Unique identifier of the product variant", example = "123e4567-e89b-12d3-a456-426614174001")
        private UUID externalVariantId;

        @Schema(description = "Unique identifier of the warehouse", example = "123e4567-e89b-12d3-a456-426614174002")
        private UUID warehouseId;

        @Schema(description = "Quantity available for reservation/sale", example = "95")
        private int quantityAvailable;

        @Schema(description = "Quantity currently reserved", example = "5")
        private int quantityReserved;

        @Schema(description = "Quantity incoming from suppliers", example = "0")
        private int quantityIncoming;

        @Schema(description = "Total physical quantity (Available + Reserved)", example = "100")
        private int totalQuantity;

        @Schema(description = "Low stock threshold", example = "10")
        private int lowStockThreshold;

        @Schema(description = "Reorder point", example = "20")
        private int reorderPoint;

        @Schema(description = "True if stock is below threshold", example = "false")
        private boolean isLowStock;

        @Schema(description = "True if stock is below reorder point", example = "false")
        private boolean needsReorder;

        @Schema(description = "Timestamp of last restock", example = "2023-10-01T10:00:00")
        private LocalDateTime lastRestockedAt;

        @Schema(description = "Record creation timestamp", example = "2023-01-01T00:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "Record update timestamp", example = "2023-10-05T12:00:00")
        private LocalDateTime updatedAt;

        // Constructors
        public StockResponse() {
        }

        public StockResponse(UUID id, UUID externalProductId, UUID externalVariantId, UUID warehouseId,
                        int quantityAvailable, int quantityReserved, int quantityIncoming, int totalQuantity,
                        int lowStockThreshold, int reorderPoint, boolean isLowStock, boolean needsReorder,
                        LocalDateTime lastRestockedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
                this.id = id;
                this.externalProductId = externalProductId;
                this.externalVariantId = externalVariantId;
                this.warehouseId = warehouseId;
                this.quantityAvailable = quantityAvailable;
                this.quantityReserved = quantityReserved;
                this.quantityIncoming = quantityIncoming;
                this.totalQuantity = totalQuantity;
                this.lowStockThreshold = lowStockThreshold;
                this.reorderPoint = reorderPoint;
                this.isLowStock = isLowStock;
                this.needsReorder = needsReorder;
                this.lastRestockedAt = lastRestockedAt;
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

        public int getQuantityAvailable() {
                return quantityAvailable;
        }

        public void setQuantityAvailable(int quantityAvailable) {
                this.quantityAvailable = quantityAvailable;
        }

        public int getQuantityReserved() {
                return quantityReserved;
        }

        public void setQuantityReserved(int quantityReserved) {
                this.quantityReserved = quantityReserved;
        }

        public int getQuantityIncoming() {
                return quantityIncoming;
        }

        public void setQuantityIncoming(int quantityIncoming) {
                this.quantityIncoming = quantityIncoming;
        }

        public int getTotalQuantity() {
                return totalQuantity;
        }

        public void setTotalQuantity(int totalQuantity) {
                this.totalQuantity = totalQuantity;
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

        public boolean isIsLowStock() {
                return isLowStock;
        }

        public void setIsLowStock(boolean isLowStock) {
                this.isLowStock = isLowStock;
        }

        public boolean isNeedsReorder() {
                return needsReorder;
        }

        public void setNeedsReorder(boolean needsReorder) {
                this.needsReorder = needsReorder;
        }

        public LocalDateTime getLastRestockedAt() {
                return lastRestockedAt;
        }

        public void setLastRestockedAt(LocalDateTime lastRestockedAt) {
                this.lastRestockedAt = lastRestockedAt;
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
