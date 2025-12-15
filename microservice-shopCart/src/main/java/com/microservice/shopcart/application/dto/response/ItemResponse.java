package com.microservice.shopcart.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for cart item with enriched product and store information.
 */
@Schema(description = "Cart item response with product and store details")
public class ItemResponse {

    @Schema(description = "Item ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    @Schema(description = "Product information from Catalog service")
    private ProductInfo product;

    @Schema(description = "Store information from Store service")
    private StoreInfo store;

    @Schema(description = "Item quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price per item", example = "49.99")
    private BigDecimal unitPrice;

    @Schema(description = "Line total (quantity * unit price)", example = "99.98")
    private BigDecimal lineTotal;

    @Schema(description = "Item added timestamp", example = "2024-12-13T10:30:00Z")
    private Instant addedAt;

    @Schema(description = "Last update timestamp", example = "2024-12-13T11:00:00Z")
    private Instant updatedAt;

    // Constructors
    public ItemResponse() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public StoreInfo getStore() {
        return store;
    }

    public void setStore(StoreInfo store) {
        this.store = store;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Nested DTO for product information.
     */
    @Schema(description = "Product information from Catalog microservice")
    public static class ProductInfo {
        
        @Schema(description = "Product ID", example = "550e8400-e29b-41d4-a716-446655440020")
        private UUID productId;
        
        @Schema(description = "Variant ID", example = "550e8400-e29b-41d4-a716-446655440021")
        private UUID variantId;
        
        @Schema(description = "Product name", example = "Wireless Bluetooth Headphones")
        private String name;
        
        @Schema(description = "Variant name", example = "Black - Large")
        private String variantName;
        
        @Schema(description = "Product SKU", example = "WBH-BLK-L-001")
        private String sku;
        
        @Schema(description = "Product image URL", example = "https://cdn.example.com/products/headphones.jpg")
        private String imageUrl;

        public ProductInfo() {
        }

        public ProductInfo(UUID productId, UUID variantId, String name, String variantName, 
                          String sku, String imageUrl) {
            this.productId = productId;
            this.variantId = variantId;
            this.name = name;
            this.variantName = variantName;
            this.sku = sku;
            this.imageUrl = imageUrl;
        }

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        public UUID getVariantId() {
            return variantId;
        }

        public void setVariantId(UUID variantId) {
            this.variantId = variantId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVariantName() {
            return variantName;
        }

        public void setVariantName(String variantName) {
            this.variantName = variantName;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    /**
     * Nested DTO for store information.
     */
    @Schema(description = "Store information from Store microservice")
    public static class StoreInfo {
        
        @Schema(description = "Store ID", example = "550e8400-e29b-41d4-a716-446655440030")
        private UUID storeId;
        
        @Schema(description = "Store name", example = "TechStore Pro")
        private String name;
        
        @Schema(description = "Store logo URL", example = "https://cdn.example.com/stores/techstore-logo.png")
        private String logoUrl;

        public StoreInfo() {
        }

        public StoreInfo(UUID storeId, String name, String logoUrl) {
            this.storeId = storeId;
            this.name = name;
            this.logoUrl = logoUrl;
        }

        public UUID getStoreId() {
            return storeId;
        }

        public void setStoreId(UUID storeId) {
            this.storeId = storeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }
}
