package com.microservice.shopcart.domain.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a coupon applied to the shopping cart.
 */
public class CouponApplied {
    
    private UUID id;
    private String code;
    private Money discountAmount;
    private String discountType; // PERCENTAGE, FIXED_AMOUNT
    private Instant createdAt;
    private Instant updatedAt;
    private Instant appliedAt;
    
    private CouponApplied() {}
    
    public static Builder builder() {
        return new Builder();
    }
    
    public boolean isPercentageDiscount() {
        return "PERCENTAGE".equalsIgnoreCase(discountType);
    }
    
    public boolean isFixedAmount() {
        return "FIXED_AMOUNT".equalsIgnoreCase(discountType);
    }
    
    // Getters
    public UUID getId() {
        return id;
    }
    
    public String getCode() {
        return code;
    }
    
    public Money getDiscountAmount() {
        return discountAmount;
    }
    
    public String getDiscountType() {
        return discountType;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public Instant getAppliedAt() {
        return appliedAt;
    }
    
    void setId(UUID id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CouponApplied coupon)) return false;
        return Objects.equals(id, coupon.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // Builder
    public static class Builder {
        private final CouponApplied coupon = new CouponApplied();
        
        public Builder id(UUID id) {
            coupon.id = id;
            return this;
        }
        
        public Builder code(String code) {
            coupon.code = code;
            return this;
        }
        
        public Builder discountAmount(Money discountAmount) {
            coupon.discountAmount = discountAmount;
            return this;
        }
        
        public Builder discountType(String discountType) {
            coupon.discountType = discountType;
            return this;
        }
        
        public Builder createdAt(Instant createdAt) {
            coupon.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(Instant updatedAt) {
            coupon.updatedAt = updatedAt;
            return this;
        }
        
        public Builder appliedAt(Instant appliedAt) {
            coupon.appliedAt = appliedAt;
            return this;
        }
        
        public CouponApplied build() {
            Objects.requireNonNull(coupon.code, "Coupon code is required");
            Objects.requireNonNull(coupon.discountAmount, "Discount amount is required");
            Objects.requireNonNull(coupon.discountType, "Discount type is required");
            
            if (coupon.id == null) {
                coupon.id = UUID.randomUUID();
            }
            if (coupon.createdAt == null) {
                coupon.createdAt = Instant.now();
            }
            if (coupon.updatedAt == null) {
                coupon.updatedAt = Instant.now();
            }
            if (coupon.appliedAt == null) {
                coupon.appliedAt = Instant.now();
            }
            
            return coupon;
        }
    }
}
