package com.microservice.shopcart.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Response DTO for cart totals calculation.
 */
@Schema(description = "Cart totals calculation result")
public class CartTotalsResponse {

    @Schema(description = "Cart subtotal before discounts", example = "149.97")
    private BigDecimal subtotal;

    @Schema(description = "Total discount amount", example = "15.00")
    private BigDecimal discountAmount;

    @Schema(description = "Cart total after discounts", example = "134.97")
    private BigDecimal total;

    @Schema(description = "Number of items in cart", example = "5")
    private Integer itemCount;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    // Constructors
    public CartTotalsResponse() {
    }

    public CartTotalsResponse(BigDecimal subtotal, BigDecimal discountAmount, 
                               BigDecimal total, Integer itemCount, String currency) {
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.total = total;
        this.itemCount = itemCount;
        this.currency = currency;
    }

    // Getters and Setters
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
