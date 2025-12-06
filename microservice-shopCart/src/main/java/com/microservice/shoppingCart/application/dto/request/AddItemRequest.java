package com.microservice.shoppingCart.application.dto.request;

import java.math.BigDecimal;

public class AddItemRequest {
    private String externalProductId;
    private String externalVariantId;
    private String externalStoreId;
    private String productName;
    private String variantName;
    private String sku;
    private String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;
    private String externalInventoryReservationId;

    public AddItemRequest() {}

    // getters / setters
    public String getExternalProductId() { return externalProductId; }
    public void setExternalProductId(String externalProductId) { this.externalProductId = externalProductId; }
    public String getExternalVariantId() { return externalVariantId; }
    public void setExternalVariantId(String externalVariantId) { this.externalVariantId = externalVariantId; }
    public String getExternalStoreId() { return externalStoreId; }
    public void setExternalStoreId(String externalStoreId) { this.externalStoreId = externalStoreId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getExternalInventoryReservationId() { return externalInventoryReservationId; }
    public void setExternalInventoryReservationId(String externalInventoryReservationId) { this.externalInventoryReservationId = externalInventoryReservationId; }
}
