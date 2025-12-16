package com.microservice.inventory.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing warehouse details")
public class WarehouseResponse {

        @Schema(description = "Unique identifier of the warehouse", example = "123e4567-e89b-12d3-a456-426614174002")
        private UUID id;

        @Schema(description = "External identifier of the physical store", example = "123e4567-e89b-12d3-a456-426614174005")
        private UUID externalStoreId;

        @Schema(description = "Name of the warehouse", example = "Central Warehouse")
        private String name;

        @Schema(description = "Unique code for the warehouse", example = "WH-001")
        private String code;

        @Schema(description = "Street address", example = "123 Logistics Way")
        private String address;

        @Schema(description = "City", example = "New York")
        private String city;

        @Schema(description = "Country", example = "USA")
        private String country;

        @Schema(description = "Whether the warehouse is active", example = "true")
        private boolean isActive;

        @Schema(description = "Creation timestamp", example = "2023-01-01T00:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "Update timestamp", example = "2023-01-01T00:00:00")
        private LocalDateTime updatedAt;

        // Constructors
        public WarehouseResponse() {
        }

        public WarehouseResponse(UUID id, UUID externalStoreId, String name, String code, String address, String city,
                        String country, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
                this.id = id;
                this.externalStoreId = externalStoreId;
                this.name = name;
                this.code = code;
                this.address = address;
                this.city = city;
                this.country = country;
                this.isActive = isActive;
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

        public UUID getExternalStoreId() {
                return externalStoreId;
        }

        public void setExternalStoreId(UUID externalStoreId) {
                this.externalStoreId = externalStoreId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        public boolean isActive() {
                return isActive;
        }

        public void setActive(boolean active) {
                isActive = active;
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
