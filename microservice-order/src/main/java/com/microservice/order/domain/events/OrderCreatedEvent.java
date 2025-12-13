package com.microservice.order.domain.events;

import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.models.OrderItem;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Event published when a new order is created.
 */
@Getter
public class OrderCreatedEvent extends DomainEvent {

    private final String orderNumber;
    private final UUID customerId;
    private final UUID storeId;
    private final List<OrderItemPayload> items;
    private final BigDecimal grandTotal;
    private final String currency;

    private OrderCreatedEvent(Order order) {
        super(order.getId(), "OrderCreated");
        this.orderNumber = order.getOrderNumber();
        this.customerId = order.getExternalUserProfileId();
        this.storeId = order.getExternalStoreId();
        this.items = order.getItems().stream()
                .map(OrderItemPayload::from)
                .toList();
        this.grandTotal = order.getGrandTotal().amount();
        this.currency = order.getCurrency();
    }

    public static OrderCreatedEvent of(Order order) {
        return new OrderCreatedEvent(order);
    }

    @Getter
    public static class OrderItemPayload {
        private final UUID itemId;
        private final UUID productId;
        private final String productName;
        private final int quantity;
        private final BigDecimal lineTotal;

        private OrderItemPayload(OrderItem item) {
            this.itemId = item.getId();
            this.productId = item.getExternalProductId();
            this.productName = item.getProductName();
            this.quantity = item.getQuantity();
            this.lineTotal = item.getLineTotal().amount();
        }

        public static OrderItemPayload from(OrderItem item) {
            return new OrderItemPayload(item);
        }
    }
}
