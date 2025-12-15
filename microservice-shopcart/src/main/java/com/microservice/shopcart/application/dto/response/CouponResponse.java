package com.microservice.shopcart.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for applied coupon information.
 */
@Schema(description = "Applied coupon response")
public class CouponResponse {

    @Schema(description = "Coupon ID", example = "550e8400-e29b-41d4-a716-446655440050")
    private UUID id;

    @Schema(description = "Coupon code", example = "SUMMER2024")
    private String code;

    @Schema(description = "Discount type", example = "PERCENTAGE", allowableValues = {"PERCENTAGE", "FIXED_AMOUNT"})
    private String discountType;

    @Schema(description = "Discount amount applied", example = "15.00")
    private BigDecimal discountAmount;

    @Schema(description = "Coupon applied timestamp", example = "2024-12-13T11:30:00Z")
    private Instant appliedAt;

    // Constructors
    public CouponResponse() {
    }

    public CouponResponse(UUID id, String code, String discountType, BigDecimal discountAmount, Instant appliedAt) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.appliedAt = appliedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }
}
