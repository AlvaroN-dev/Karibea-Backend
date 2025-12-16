package com.microservice.shopcart.domain.models;

import com.microservice.shopcart.domain.events.*;
import com.microservice.shopcart.domain.exceptions.*;
import com.microservice.shopcart.domain.models.enums.CartStatus;

import java.time.Instant;
import java.util.*;

/**
 * Aggregate Root for the Shopping Cart bounded context.
 * Enforces all business invariants and manages the lifecycle of cart items and coupons.
 */
public class ShoppingCart {
    
    private static final int MAX_ITEMS = 100;
    
    private UUID id;
    private UUID externalUserProfileId;
    private String sessionId;
    private Long statusId;
    private CartStatus status;
    private String currency;
    private Money subtotal;
    private Money total;
    private int itemCount;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant expiresAt;
    private Instant deletedAt;
    private boolean isDeleted;
    
    private List<Item> items = new ArrayList<>();
    private List<CouponApplied> coupons = new ArrayList<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    private ShoppingCart() {}
    
    /**
     * Factory method to create a new shopping cart.
     */
    public static ShoppingCart create(UUID userProfileId, String sessionId, String currency) {
        ShoppingCart cart = new ShoppingCart();
        cart.id = UUID.randomUUID();
        cart.externalUserProfileId = userProfileId;
        cart.sessionId = sessionId;
        cart.currency = currency != null ? currency.toUpperCase() : "USD";
        cart.status = CartStatus.ACTIVE;
        cart.statusId = 1L; // ACTIVE status
        cart.subtotal = Money.zero(cart.currency);
        cart.total = Money.zero(cart.currency);
        cart.itemCount = 0;
        cart.createdAt = Instant.now();
        cart.updatedAt = Instant.now();
        cart.expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days
        cart.isDeleted = false;
        
        cart.registerEvent(new CartCreatedEvent(cart.id, userProfileId, sessionId, cart.createdAt));
        
        return cart;
    }
    
    /**
     * Adds an item to the cart or increases quantity if already exists.
     */
    public void addItem(Item item) {
        validateCartIsActive();
        validateNotExpired();
        
        if (items.size() >= MAX_ITEMS) {
            throw new InvalidCartOperationException(
                String.format("Cart cannot have more than %d items", MAX_ITEMS));
        }
        
        // Check if product/variant already exists in cart
        Optional<Item> existingItem = findItemByProductAndVariant(
            item.getExternalProductId(), 
            item.getExternalVariantId()
        );
        
        if (existingItem.isPresent()) {
            // Merge quantities
            Item existing = existingItem.get();
            Quantity newQuantity = existing.getQuantity().add(item.getQuantity().getValue());
            existing.updateQuantity(newQuantity);
        } else {
            items.add(item);
        }
        
        recalculateTotals();
        this.updatedAt = Instant.now();
        
        registerEvent(new ItemAddedToCartEvent(
            this.id, 
            item.getId(),
            item.getExternalProductId(),
            item.getQuantity().getValue(),
            item.getUnitPrice().getAmount(),
            Instant.now()
        ));
    }
    
    /**
     * Updates the quantity of an existing item.
     */
    public void updateItemQuantity(UUID itemId, Quantity newQuantity) {
        validateCartIsActive();
        validateNotExpired();
        
        Item item = findItemById(itemId)
            .orElseThrow(() -> new ItemNotFoundException(itemId));
        
        item.updateQuantity(newQuantity);
        recalculateTotals();
        this.updatedAt = Instant.now();
        
        registerEvent(new ItemQuantityUpdatedEvent(
            this.id,
            itemId,
            newQuantity.getValue(),
            Instant.now()
        ));
    }
    
    /**
     * Removes an item from the cart.
     */
    public void removeItem(UUID itemId) {
        validateCartIsActive();
        validateNotExpired();
        
        Item item = findItemById(itemId)
            .orElseThrow(() -> new ItemNotFoundException(itemId));
        
        items.remove(item);
        recalculateTotals();
        this.updatedAt = Instant.now();
        
        registerEvent(new ItemRemovedFromCartEvent(
            this.id,
            itemId,
            item.getExternalProductId(),
            Instant.now()
        ));
    }
    
    /**
     * Applies a coupon to the cart.
     */
    public void applyCoupon(CouponApplied coupon) {
        validateCartIsActive();
        validateNotExpired();
        
        // Check if coupon already applied
        boolean alreadyApplied = coupons.stream()
            .anyMatch(c -> c.getCode().equalsIgnoreCase(coupon.getCode()));
        
        if (alreadyApplied) {
            throw new CouponAlreadyAppliedException(coupon.getCode());
        }
        
        coupons.add(coupon);
        recalculateTotals();
        this.updatedAt = Instant.now();
        
        registerEvent(new CouponAppliedToCartEvent(
            this.id,
            coupon.getCode(),
            coupon.getDiscountAmount().getAmount(),
            Instant.now()
        ));
    }
    
    /**
     * Removes a coupon from the cart.
     */
    public void removeCoupon(UUID couponId) {
        validateCartIsActive();
        
        CouponApplied coupon = coupons.stream()
            .filter(c -> c.getId().equals(couponId))
            .findFirst()
            .orElseThrow(() -> new CouponNotFoundException(couponId));
        
        coupons.remove(coupon);
        recalculateTotals();
        this.updatedAt = Instant.now();
    }
    
    /**
     * Expires the cart due to inactivity.
     */
    public void expire() {
        if (status.isTerminal()) {
            throw new InvalidCartOperationException("Cannot expire a cart that is already in terminal state");
        }
        
        this.status = CartStatus.EXPIRED;
        this.statusId = 3L; // EXPIRED
        this.updatedAt = Instant.now();
        
        registerEvent(new CartExpiredEvent(
            this.id,
            this.externalUserProfileId,
            getItemProductIds(),
            Instant.now()
        ));
    }
    
    /**
     * Marks the cart as converted to an order.
     */
    public void markAsConverted() {
        validateCartIsActive();
        
        if (items.isEmpty()) {
            throw new InvalidCartOperationException("Cannot convert an empty cart");
        }
        
        this.status = CartStatus.CONVERTED;
        this.statusId = 4L; // CONVERTED
        this.updatedAt = Instant.now();
    }
    
    /**
     * Soft deletes the cart.
     */
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
        this.updatedAt = Instant.now();
        
        registerEvent(new CartDeletedEvent(
            this.id,
            this.externalUserProfileId,
            Instant.now()
        ));
    }
    
    /**
     * Updates cart notes.
     */
    public void updateNotes(String notes) {
        this.notes = notes;
        this.updatedAt = Instant.now();
    }
    
    // Private helper methods
    
    private void validateCartIsActive() {
        if (!status.canAddItems()) {
            throw new InvalidCartOperationException(
                String.format("Cart is %s and cannot be modified", status.getName()));
        }
    }
    
    private void validateNotExpired() {
        if (Instant.now().isAfter(expiresAt)) {
            expire();
            throw new CartExpiredException(this.id);
        }
    }
    
    private void recalculateTotals() {
        // Calculate subtotal from items
        this.subtotal = items.stream()
            .map(Item::getLineTotal)
            .reduce(Money.zero(this.currency), Money::add);
        
        // Calculate total discount
        Money totalDiscount = coupons.stream()
            .map(CouponApplied::getDiscountAmount)
            .reduce(Money.zero(this.currency), Money::add);
        
        // Calculate final total
        this.total = subtotal.subtract(totalDiscount);
        if (this.total.isNegative()) {
            this.total = Money.zero(this.currency);
        }
        
        // Update item count
        this.itemCount = items.stream()
            .mapToInt(i -> i.getQuantity().getValue())
            .sum();
    }
    
    private Optional<Item> findItemById(UUID itemId) {
        return items.stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst();
    }
    
    private Optional<Item> findItemByProductAndVariant(UUID productId, UUID variantId) {
        return items.stream()
            .filter(i -> i.getExternalProductId().equals(productId) &&
                        Objects.equals(i.getExternalVariantId(), variantId))
            .findFirst();
    }
    
    private List<UUID> getItemProductIds() {
        return items.stream()
            .map(Item::getExternalProductId)
            .toList();
    }
    
    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    public void clearDomainEvents() {
        domainEvents.clear();
    }
    
    // Getters
    public UUID getId() {
        return id;
    }
    
    public UUID getExternalUserProfileId() {
        return externalUserProfileId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public Long getStatusId() {
        return statusId;
    }
    
    public CartStatus getStatus() {
        return status;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public Money getSubtotal() {
        return subtotal;
    }
    
    public Money getTotal() {
        return total;
    }
    
    public int getItemCount() {
        return itemCount;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public Instant getExpiresAt() {
        return expiresAt;
    }
    
    public Instant getDeletedAt() {
        return deletedAt;
    }
    
    public boolean isDeleted() {
        return isDeleted;
    }
    
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public List<CouponApplied> getCoupons() {
        return Collections.unmodifiableList(coupons);
    }
    
    // Builder for reconstruction from persistence
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private final ShoppingCart cart = new ShoppingCart();
        
        public Builder id(UUID id) {
            cart.id = id;
            return this;
        }
        
        public Builder externalUserProfileId(UUID externalUserProfileId) {
            cart.externalUserProfileId = externalUserProfileId;
            return this;
        }
        
        public Builder sessionId(String sessionId) {
            cart.sessionId = sessionId;
            return this;
        }
        
        public Builder statusId(Long statusId) {
            cart.statusId = statusId;
            return this;
        }
        
        public Builder status(CartStatus status) {
            cart.status = status;
            return this;
        }
        
        public Builder currency(String currency) {
            cart.currency = currency;
            return this;
        }
        
        public Builder subtotal(Money subtotal) {
            cart.subtotal = subtotal;
            return this;
        }
        
        public Builder total(Money total) {
            cart.total = total;
            return this;
        }
        
        public Builder itemCount(int itemCount) {
            cart.itemCount = itemCount;
            return this;
        }
        
        public Builder notes(String notes) {
            cart.notes = notes;
            return this;
        }
        
        public Builder createdAt(Instant createdAt) {
            cart.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(Instant updatedAt) {
            cart.updatedAt = updatedAt;
            return this;
        }
        
        public Builder expiresAt(Instant expiresAt) {
            cart.expiresAt = expiresAt;
            return this;
        }
        
        public Builder deletedAt(Instant deletedAt) {
            cart.deletedAt = deletedAt;
            return this;
        }
        
        public Builder isDeleted(boolean isDeleted) {
            cart.isDeleted = isDeleted;
            return this;
        }
        
        public Builder items(List<Item> items) {
            cart.items = new ArrayList<>(items);
            return this;
        }
        
        public Builder coupons(List<CouponApplied> coupons) {
            cart.coupons = new ArrayList<>(coupons);
            return this;
        }
        
        public ShoppingCart build() {
            if (cart.id == null) {
                cart.id = UUID.randomUUID();
            }
            if (cart.status == null) {
                cart.status = CartStatus.ACTIVE;
            }
            if (cart.currency == null) {
                cart.currency = "USD";
            }
            if (cart.subtotal == null) {
                cart.subtotal = Money.zero(cart.currency);
            }
            if (cart.total == null) {
                cart.total = Money.zero(cart.currency);
            }
            if (cart.createdAt == null) {
                cart.createdAt = Instant.now();
            }
            if (cart.updatedAt == null) {
                cart.updatedAt = Instant.now();
            }
            return cart;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCart that)) return false;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
