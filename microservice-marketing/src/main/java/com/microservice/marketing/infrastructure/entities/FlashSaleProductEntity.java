package com.microservice.marketing.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "flash_sales_product")
public class FlashSaleProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flash_sale_id")
    private FlashSaleEntity flashSale;

    @Column(name = "external_product_id")
    private String externalProductId;

    @Column(name = "sale_price", precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "quantity_limit")
    private Integer quantityLimit;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlashSaleEntity getFlashSale() {
        return flashSale;
    }

    public void setFlashSale(FlashSaleEntity flashSale) {
        this.flashSale = flashSale;
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
