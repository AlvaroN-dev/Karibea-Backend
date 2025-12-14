package com.microservice.marketing.domain.model;

import java.math.BigDecimal;

public class FlashSaleProduct {
    private Long id;
    private Long flashSaleId;
    private String externalProductId;
    private BigDecimal salePrice;
    private Integer quantityLimit;
    private Integer quantitySold;

    public FlashSaleProduct() {
    }

    public FlashSaleProduct(Long id, Long flashSaleId, String externalProductId, BigDecimal salePrice,
            Integer quantityLimit, Integer quantitySold) {
        this.id = id;
        this.flashSaleId = flashSaleId;
        this.externalProductId = externalProductId;
        this.salePrice = salePrice;
        this.quantityLimit = quantityLimit;
        this.quantitySold = quantitySold;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(Long flashSaleId) {
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
