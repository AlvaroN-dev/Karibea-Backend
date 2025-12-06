package com.microservice.shoppingCart.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad agregada ShoppingCart con reglas de negocio básicas.
 */
public class ShoppingCart {

    private UUID id;
    private String externalUserProfilesId;
    private String sessionId;
    private Status status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal total;
    private int itemCount;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant expiredAt;
    private Instant deletedAt;
    private boolean isDeleted;

    private final List<Item> items;
    private final List<CouponApplied> couponsApplied;

    public ShoppingCart() {
        this.id = UUID.randomUUID();
        this.subtotal = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.itemCount = 0;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.items = new ArrayList<>();
        this.couponsApplied = new ArrayList<>();
        this.currency = "USD";
        this.isDeleted = false;
    }

    public ShoppingCart(UUID id,
                        String externalUserProfilesId,
                        String sessionId,
                        Status status,
                        String currency,
                        Instant createdAt) {
        this();
        this.id = id == null ? UUID.randomUUID() : id;
        this.externalUserProfilesId = externalUserProfilesId;
        this.sessionId = sessionId;
        this.status = status;
        this.currency = currency == null ? this.currency : currency;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    // Business rules

    /**
     * Agrega un item al carrito. Si ya existe un item con el mismo sku y variant, incrementa cantidad.
     */
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero");
        }
        if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Item unit price must be non-negative");
        }

        // buscar item semejante (por sku + externalVariantId)
        Item existing = findSimilarItem(item);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
            existing.recalculateLineTotal();
        } else {
            item.recalculateLineTotal();
            this.items.add(item);
        }

        recalculateCart();
        this.updatedAt = Instant.now();
    }

    /**
     * Elimina un item por su id. Retorna true si fue removido.
     */
    public boolean removeItemById(UUID itemId) {
        if (itemId == null) {
            return false;
        }
        boolean removed = this.items.removeIf(i -> itemId.equals(i.getId()));
        if (removed) {
            recalculateCart();
            this.updatedAt = Instant.now();
        }
        return removed;
    }

    /**
     * Aplica un cupón al carrito. Previene duplicados por código.
     */
    public void applyCoupon(CouponApplied coupon) {
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon cannot be null");
        }
        if (coupon.getCode() == null || coupon.getCode().isBlank()) {
            throw new IllegalArgumentException("Coupon code cannot be empty");
        }
        boolean exists = this.couponsApplied.stream()
                .anyMatch(c -> c.getCode().equalsIgnoreCase(coupon.getCode()));
        if (exists) {
            throw new IllegalArgumentException("Coupon already applied: " + coupon.getCode());
        }
        this.couponsApplied.add(coupon);
        recalculateCart();
        this.updatedAt = Instant.now();
    }

    /**
     * Cambia estado del carrito.
     */
    public void updateStatus(Status newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    /**
     * Recalcula subtotal, total e itemCount según items y cupones.
     */
    public void recalculateCart() {
        BigDecimal newSubtotal = items.stream()
                .map(Item::getLineTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int newItemCount = items.stream()
                .mapToInt(Item::getQuantity)
                .sum();

        BigDecimal discountTotal = couponsApplied.stream()
                .map(coupon -> coupon.calculateDiscountAmount(newSubtotal))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal newTotal = newSubtotal.subtract(discountTotal);
        if (newTotal.compareTo(BigDecimal.ZERO) < 0) {
            newTotal = BigDecimal.ZERO;
        }

        this.subtotal = newSubtotal;
        this.itemCount = newItemCount;
        this.total = newTotal;
    }

    private Item findSimilarItem(Item item) {
        for (Item i : this.items) {
            boolean sameSku = i.getSku() != null && i.getSku().equals(item.getSku());
            boolean sameVariant = i.getExternalVariantId() != null &&
                    i.getExternalVariantId().equals(item.getExternalVariantId());
            if (sameSku || sameVariant) {
                return i;
            }
        }
        return null;
    }

    // ---- Getters / Setters ----

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUserProfilesId() {
        return externalUserProfilesId;
    }

    public void setExternalUserProfilesId(String externalUserProfilesId) {
        this.externalUserProfilesId = externalUserProfilesId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public BigDecimal getTotal() {
        return total;
    }

    public int getItemCount() {
        return itemCount;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<CouponApplied> getCouponsApplied() {
        return Collections.unmodifiableList(couponsApplied);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
