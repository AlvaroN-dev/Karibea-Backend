package com.microservice.order.domain.models;

import com.microservice.order.domain.events.*;
import com.microservice.order.domain.exceptions.InvalidOrderStateTransitionException;
import com.microservice.order.domain.exceptions.OrderInvariantViolationException;
import com.microservice.order.domain.models.enums.OrderStatusEnum;
import com.microservice.order.domain.models.records.Address;
import com.microservice.order.domain.models.records.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Order Aggregate Root - the main entry point for all Order operations.
 * Encapsulates business rules and invariants.
 * 
 * PURE DOMAIN - No framework dependencies.
 */
public class Order {

    private UUID id;

    private String orderNumber;

    // External references
    private UUID externalUserProfileId;
    private UUID externalStoreId;
    private UUID externalPaymentId;
    private UUID externalShipmentId;

    // Order data
    private OrderStatusEnum status;
    private String currency;
    private Address shippingAddress;
    private Address billingAddress;
    private String notes;
    private String customerNotes;
    private String ipAddress;
    private String userAgent;

    // Totals
    private Money subtotal;
    private Money discountTotal;
    private Money taxTotal;
    private Money shippingTotal;
    private Money grandTotal;

    // Timestamps
    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Child entities
    private List<OrderItem> items;
    private List<OrderCoupon> coupons;
    private List<OrderStatusHistory> statusHistory;

    // Domain events (transient - not persisted)
    private transient List<DomainEvent> domainEvents;

    /**
     * Private constructor - use factory method create() or Builder.
     */
    private Order() {
        this.id = UUID.randomUUID();
        this.status = OrderStatusEnum.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.coupons = new ArrayList<>();
        this.statusHistory = new ArrayList<>();
        this.domainEvents = new ArrayList<>();
        this.subtotal = Money.zero();
        this.discountTotal = Money.zero();
        this.taxTotal = Money.zero();
        this.shippingTotal = Money.zero();
        this.grandTotal = Money.zero();
    }

    /**
     * Factory method to create a new Order.
     */
    public static Order create(
            UUID customerId,
            UUID storeId,
            String currency,
            Address shippingAddress,
            Address billingAddress,
            List<OrderItem> items) {

        if (customerId == null) {
            throw new OrderInvariantViolationException("Customer ID is required");
        }
        if (storeId == null) {
            throw new OrderInvariantViolationException("Store ID is required");
        }
        if (items == null || items.isEmpty()) {
            throw new OrderInvariantViolationException("Order must have at least one item");
        }

        Order order = new Order();
        order.externalUserProfileId = customerId;
        order.externalStoreId = storeId;
        order.currency = currency != null ? currency : "USD";
        order.shippingAddress = shippingAddress;
        order.billingAddress = billingAddress;
        order.orderNumber = generateOrderNumber();

        // Add items
        for (OrderItem item : items) {
            order.addItem(item);
        }

        order.recalculateTotals();

        // Register domain event
        order.registerEvent(OrderCreatedEvent.of(order));

        return order;
    }

    private static String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + datePart + "-" + randomPart;
    }

    // ========== Item Management ==========

    public void addItem(OrderItem item) {
        if (status.isFinalState()) {
            throw new OrderInvariantViolationException("Cannot add items to a finalized order");
        }
        item.setOrderId(this.id);
        this.items.add(item);
        this.updatedAt = LocalDateTime.now();
        recalculateTotals();
    }

    public void removeItem(UUID itemId) {
        if (status.isFinalState()) {
            throw new OrderInvariantViolationException("Cannot remove items from a finalized order");
        }
        items.removeIf(item -> item.getId().equals(itemId));
        if (items.isEmpty()) {
            throw new OrderInvariantViolationException("Order must have at least one item");
        }
        this.updatedAt = LocalDateTime.now();
        recalculateTotals();
    }

    // ========== Coupon Management ==========

    public void applyCoupon(OrderCoupon coupon) {
        if (status != OrderStatusEnum.PENDING) {
            throw new OrderInvariantViolationException("Coupons can only be applied to pending orders");
        }
        coupon.setOrderId(this.id);
        this.coupons.add(coupon);
        this.updatedAt = LocalDateTime.now();
        recalculateTotals();

        registerEvent(OrderCouponAppliedEvent.of(this.id, coupon.getCouponCode(), coupon.getDiscountAmount()));
    }

    // ========== Status Transitions ==========

    public void confirm(UUID paymentId, String confirmedBy) {
        validateTransition(OrderStatusEnum.CONFIRMED);
        this.externalPaymentId = paymentId;
        this.status = OrderStatusEnum.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(OrderStatusEnum.PENDING, OrderStatusEnum.CONFIRMED, confirmedBy, "Payment confirmed");
        registerEvent(OrderConfirmedEvent.of(this.id, paymentId, this.confirmedAt));
    }

    public void process(String processedBy) {
        validateTransition(OrderStatusEnum.PROCESSING);
        OrderStatusEnum previousStatus = this.status;
        this.status = OrderStatusEnum.PROCESSING;
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.PROCESSING, processedBy, "Order processing started");
        registerEvent(OrderStatusChangedEvent.of(this.id, previousStatus, OrderStatusEnum.PROCESSING,
                "Order processing started"));
    }

    public void ship(UUID shipmentId, String shippedBy) {
        validateTransition(OrderStatusEnum.SHIPPED);
        OrderStatusEnum previousStatus = this.status;
        this.externalShipmentId = shipmentId;
        this.status = OrderStatusEnum.SHIPPED;
        this.shippedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.SHIPPED, shippedBy, "Order shipped");
        registerEvent(OrderShippedEvent.of(this.id, shipmentId, this.shippedAt));
    }

    public void deliver(String deliveredBy) {
        validateTransition(OrderStatusEnum.DELIVERED);
        OrderStatusEnum previousStatus = this.status;
        this.status = OrderStatusEnum.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.DELIVERED, deliveredBy, "Order delivered");
        registerEvent(OrderDeliveredEvent.of(this.id, this.deliveredAt));
    }

    public void complete(String completedBy) {
        validateTransition(OrderStatusEnum.COMPLETED);
        OrderStatusEnum previousStatus = this.status;
        this.status = OrderStatusEnum.COMPLETED;
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.COMPLETED, completedBy, "Order completed");
        registerEvent(
                OrderStatusChangedEvent.of(this.id, previousStatus, OrderStatusEnum.COMPLETED, "Order completed"));
    }

    public void cancel(String reason, String cancelledBy) {
        if (!status.isCancellable()) {
            throw new InvalidOrderStateTransitionException(
                    "Cannot cancel order in status: " + status.name());
        }
        OrderStatusEnum previousStatus = this.status;
        this.status = OrderStatusEnum.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.CANCELLED, cancelledBy, reason);
        registerEvent(OrderCancelledEvent.of(this.id, reason, this.cancelledAt));
    }

    public void initiateReturn(String reason, String returnedBy) {
        validateTransition(OrderStatusEnum.RETURNED);
        OrderStatusEnum previousStatus = this.status;
        this.status = OrderStatusEnum.RETURNED;
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.RETURNED, returnedBy, reason);
        registerEvent(OrderStatusChangedEvent.of(this.id, previousStatus, OrderStatusEnum.RETURNED, reason));
    }

    public void refund(String refundedBy) {
        validateTransition(OrderStatusEnum.REFUNDED);
        OrderStatusEnum previousStatus = this.status;
        this.status = OrderStatusEnum.REFUNDED;
        this.updatedAt = LocalDateTime.now();

        addStatusHistory(previousStatus, OrderStatusEnum.REFUNDED, refundedBy, "Order refunded");
        registerEvent(OrderStatusChangedEvent.of(this.id, previousStatus, OrderStatusEnum.REFUNDED, "Order refunded"));
    }

    // ========== Private Helpers ==========

    private void validateTransition(OrderStatusEnum targetStatus) {
        if (!status.canTransitionTo(targetStatus)) {
            throw new InvalidOrderStateTransitionException(
                    String.format("Cannot transition from %s to %s", status.name(), targetStatus.name()));
        }
    }

    private void addStatusHistory(OrderStatusEnum from, OrderStatusEnum to, String changedBy, String reason) {
        OrderStatusHistory history = OrderStatusHistory.create(this.id, from, to, changedBy, reason);
        this.statusHistory.add(history);
    }

    public void recalculateTotals() {
        // Calculate subtotal from items
        BigDecimal itemsTotal = items.stream()
                .map(item -> item.getLineTotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.subtotal = Money.of(itemsTotal);

        // Calculate discount from items and coupons
        BigDecimal itemDiscounts = items.stream()
                .map(item -> item.getDiscountAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal couponDiscounts = coupons.stream()
                .map(coupon -> coupon.getDiscountAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.discountTotal = Money.of(itemDiscounts.add(couponDiscounts));

        // Calculate tax from items
        BigDecimal taxes = items.stream()
                .map(item -> item.getTaxAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxTotal = Money.of(taxes);

        // Grand total calculation
        BigDecimal grand = subtotal.amount()
                .subtract(discountTotal.amount())
                .add(taxTotal.amount())
                .add(shippingTotal != null ? shippingTotal.amount() : BigDecimal.ZERO);

        if (grand.compareTo(BigDecimal.ZERO) < 0) {
            grand = BigDecimal.ZERO;
        }
        this.grandTotal = Money.of(grand);
    }

    public void setShippingTotal(Money shippingTotal) {
        this.shippingTotal = shippingTotal;
        this.updatedAt = LocalDateTime.now();
        recalculateTotals();
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
        this.updatedAt = LocalDateTime.now();
    }

    public void setMetadata(String ipAddress, String userAgent) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    // ========== Domain Events ==========

    private void registerEvent(DomainEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // ========== Getters (Pure Java) ==========

    public UUID getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public UUID getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public UUID getExternalStoreId() {
        return externalStoreId;
    }

    public UUID getExternalPaymentId() {
        return externalPaymentId;
    }

    public UUID getExternalShipmentId() {
        return externalShipmentId;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getNotes() {
        return notes;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public Money getDiscountTotal() {
        return discountTotal;
    }

    public Money getTaxTotal() {
        return taxTotal;
    }

    public Money getShippingTotal() {
        return shippingTotal;
    }

    public Money getGrandTotal() {
        return grandTotal;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public LocalDateTime getShippedAt() {
        return shippedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<OrderCoupon> getCoupons() {
        return Collections.unmodifiableList(coupons);
    }

    public List<OrderStatusHistory> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }

    // ========== Builder for Reconstitution ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Order order = new Order();

        public Builder id(UUID id) {
            order.id = id;
            return this;
        }

        public Builder orderNumber(String orderNumber) {
            order.orderNumber = orderNumber;
            return this;
        }

        public Builder externalUserProfileId(UUID id) {
            order.externalUserProfileId = id;
            return this;
        }

        public Builder externalStoreId(UUID id) {
            order.externalStoreId = id;
            return this;
        }

        public Builder externalPaymentId(UUID id) {
            order.externalPaymentId = id;
            return this;
        }

        public Builder externalShipmentId(UUID id) {
            order.externalShipmentId = id;
            return this;
        }

        public Builder status(OrderStatusEnum status) {
            order.status = status;
            return this;
        }

        public Builder currency(String currency) {
            order.currency = currency;
            return this;
        }

        public Builder shippingAddress(Address address) {
            order.shippingAddress = address;
            return this;
        }

        public Builder billingAddress(Address address) {
            order.billingAddress = address;
            return this;
        }

        public Builder notes(String notes) {
            order.notes = notes;
            return this;
        }

        public Builder customerNotes(String notes) {
            order.customerNotes = notes;
            return this;
        }

        public Builder ipAddress(String ip) {
            order.ipAddress = ip;
            return this;
        }

        public Builder userAgent(String ua) {
            order.userAgent = ua;
            return this;
        }

        public Builder subtotal(Money subtotal) {
            order.subtotal = subtotal;
            return this;
        }

        public Builder discountTotal(Money discount) {
            order.discountTotal = discount;
            return this;
        }

        public Builder taxTotal(Money tax) {
            order.taxTotal = tax;
            return this;
        }

        public Builder shippingTotal(Money shipping) {
            order.shippingTotal = shipping;
            return this;
        }

        public Builder grandTotal(Money grand) {
            order.grandTotal = grand;
            return this;
        }

        public Builder confirmedAt(LocalDateTime dt) {
            order.confirmedAt = dt;
            return this;
        }

        public Builder shippedAt(LocalDateTime dt) {
            order.shippedAt = dt;
            return this;
        }

        public Builder deliveredAt(LocalDateTime dt) {
            order.deliveredAt = dt;
            return this;
        }

        public Builder cancelledAt(LocalDateTime dt) {
            order.cancelledAt = dt;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            order.createdAt = dt;
            return this;
        }

        public Builder updatedAt(LocalDateTime dt) {
            order.updatedAt = dt;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            order.items = new ArrayList<>(items);
            return this;
        }

        public Builder coupons(List<OrderCoupon> coupons) {
            order.coupons = new ArrayList<>(coupons);
            return this;
        }

        public Builder statusHistory(List<OrderStatusHistory> history) {
            order.statusHistory = new ArrayList<>(history);
            return this;
        }

        public Order build() {
            return order;
        }
    }
}
