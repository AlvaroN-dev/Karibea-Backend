package com.microservice.marketing.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class FlashSaleProduct {
    private UUID id;
    private UUID flashSaleId;
    private String externalProductId;
    private BigDecimal salePrice;
    private Integer quantityLimit;
    private Integer quantitySold;

    public FlashSaleProduct() {
    }

    public FlashSaleProduct(UUID id, UUID flashSaleId, String externalProductId, BigDecimal salePrice,
            Integer quantityLimit, Integer quantitySold) {
        this.id = id;
        this.flashSaleId = flashSaleId;
        this.externalProductId = externalProductId;
        this.salePrice = salePrice;
        this.quantityLimit = quantityLimit;
        this.quantitySold = quantitySold;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(UUID flashSaleId) {
        this.flashSaleId = flashSaleId;
    }

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getQuantityLimit() {
        return quantityLimit;
    }

    public void setQuantityLimit(Integer quantityLimit) {
        this.quantityLimit = quantityLimit;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }
}
