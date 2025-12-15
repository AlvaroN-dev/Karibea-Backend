package com.microservice.shopcart.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for shopping_carts table.
 */
@Entity
@Table(name = "shopping_carts")
public class ShoppingCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "external_user_profiles_id", columnDefinition = "uuid")
    private UUID externalUserProfilesId;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private StatusEntity status;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "item_count")
    private Integer itemCount;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "create_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updatedAt;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_delete")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CouponAppliedEntity> coupons = new ArrayList<>();

    // Helper methods for bidirectional relationships
    public void addItem(ItemEntity item) {
        items.add(item);
        item.setShoppingCart(this);
    }

    public void removeItem(ItemEntity item) {
        items.remove(item);
        item.setShoppingCart(null);
    }

    public void addCoupon(CouponAppliedEntity coupon) {
        coupons.add(coupon);
        coupon.setShoppingCart(this);
    }

    public void removeCoupon(CouponAppliedEntity coupon) {
        coupons.remove(coupon);
        coupon.setShoppingCart(null);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExternalUserProfilesId() {
        return externalUserProfilesId;
    }

    public void setExternalUserProfilesId(UUID externalUserProfilesId) {
        this.externalUserProfilesId = externalUserProfilesId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public List<CouponAppliedEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponAppliedEntity> coupons) {
        this.coupons = coupons;
    }
}
