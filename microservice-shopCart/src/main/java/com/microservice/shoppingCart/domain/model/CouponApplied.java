package com.microservice.shoppingCart.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa un cupón aplicado al carrito.
 */
public class CouponApplied {

    private UUID id;
    private UUID shoppingCartId;
    private String code;
    private BigDecimal discountAmount; // si discountType == FIXED, cantidad fija; si PERCENTAGE, porcentaje (0-100)
    private DiscountType discountType;
    private Instant createdAt;
    private Instant appliedAt;
    private Instant updatedAt;

    public enum DiscountType {
        FIXED,
        PERCENTAGE
    }

    public CouponApplied() {
        this.id = UUID.randomUUID();
        this.discountAmount = BigDecimal.ZERO;
        this.discountType = DiscountType.FIXED;
        this.createdAt = Instant.now();
        this.appliedAt = this.createdAt;
        this.updatedAt = this.createdAt;
    }

    public CouponApplied(UUID id, String code, BigDecimal discountAmount, DiscountType discountType) {
        this();
        this.id = id == null ? UUID.randomUUID() : id;
        this.code = code;
        this.discountAmount = discountAmount == null ? BigDecimal.ZERO : discountAmount;
        this.discountType = discountType == null ? DiscountType.FIXED : discountType;
    }

    /**
     * Calcula el monton de descuento sobre un subtotal dado.
     */
    public BigDecimal calculateDiscountAmount(BigDecimal subtotal) {
        if (subtotal == null) {
            return BigDecimal.ZERO;
        }
        if (discountType == DiscountType.FIXED) {
            BigDecimal applied = discountAmount.min(subtotal);
            return applied.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : applied;
        } else {
            // porcentaje: discountAmount representa porcentaje (por ejemplo 10 => 10%)
            BigDecimal percent = discountAmount.divide(BigDecimal.valueOf(100));
            BigDecimal applied = subtotal.multiply(percent);
            return applied.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : applied;
        }
    }

    // Getters / Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(UUID shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount == null ? BigDecimal.ZERO : discountAmount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouponApplied that = (CouponApplied) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
