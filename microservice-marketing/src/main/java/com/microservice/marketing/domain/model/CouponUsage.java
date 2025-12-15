package com.microservice.marketing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CouponUsage {
    private UUID id;
    private UUID couponId;
    private String externalUserProfileId;
    private String externalOrderId;
    private BigDecimal discountAmount;
    private LocalDateTime usedAt;

    public CouponUsage() {
    }

    public CouponUsage(UUID id, UUID couponId, String externalUserProfileId, String externalOrderId,
            BigDecimal discountAmount, LocalDateTime usedAt) {
        this.id = id;
        this.couponId = couponId;
        this.externalUserProfileId = externalUserProfileId;
        this.externalOrderId = externalOrderId;
        this.discountAmount = discountAmount;
        this.usedAt = usedAt;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCouponId() {
        return couponId;
    }

    public void setCouponId(UUID couponId) {
        this.couponId = couponId;
    }

    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public void setExternalUserProfileId(String externalUserProfileId) {
        this.externalUserProfileId = externalUserProfileId;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
