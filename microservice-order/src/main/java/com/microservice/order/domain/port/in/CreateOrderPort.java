package com.microservice.order.domain.port.in;

import com.microservice.order.domain.models.Order;

import java.util.UUID;

/**
 * Inbound port for creating orders.
 */
public interface CreateOrderPort {

    Order execute(CreateOrderCommand command);

    record CreateOrderCommand(
            UUID customerId,
            UUID storeId,
            String currency,
            String shippingStreet,
            String shippingCity,
            String shippingState,
            String shippingZipCode,
            String shippingCountry,
            String billingStreet,
            String billingCity,
            String billingState,
            String billingZipCode,
            String billingCountry,
            java.util.List<OrderItemCommand> items,
            String customerNotes,
            String ipAddress,
            String userAgent) {
    }

    record OrderItemCommand(
            UUID productId,
            String productName,
            String variantName,
            String sku,
            String imageUrl,
            java.math.BigDecimal unitPrice,
            int quantity) {
    }
}
