package com.microservice.shoppingCart.application.dto.request;

import java.math.BigDecimal;

public class ApplyCouponRequest {
    private String code;
    private BigDecimal discountAmount;
    private String discountType;

    public ApplyCouponRequest() {}

    // getters/setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
}
