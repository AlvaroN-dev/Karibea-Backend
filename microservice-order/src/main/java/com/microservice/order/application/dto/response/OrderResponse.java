package com.microservice.order.application.dto.response;

import com.microservice.order.domain.models.enums.OrderStatusEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Order.
 */
@Builder
public record OrderResponse(
        UUID id,
        String orderNumber,
        UUID customerId,
        UUID storeId,
        OrderStatusEnum status,
        String currency,
        AddressResponse shippingAddress,
        AddressResponse billingAddress,
        List<OrderItemResponse> items,
        List<OrderCouponResponse> coupons,
        BigDecimal subtotal,
        BigDecimal discountTotal,
        BigDecimal taxTotal,
        BigDecimal shippingTotal,
        BigDecimal grandTotal,
        String customerNotes,
        LocalDateTime confirmedAt,
        LocalDateTime shippedAt,
        LocalDateTime deliveredAt,
        LocalDateTime cancelledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    @Builder
    public record AddressResponse(
            String street,
            String city,
            String state,
            String zipCode,
            String country,
            String fullAddress) {
    }

    @Builder
    public record OrderItemResponse(
            UUID id,
            UUID productId,
            String productName,
            String variantName,
            String sku,
            String imageUrl,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal discountAmount,
            BigDecimal taxAmount,
            BigDecimal lineTotal,
            String status) {
    }

    @Builder
    public record OrderCouponResponse(
            UUID id,
            String couponCode,
            BigDecimal discountAmount,
            LocalDateTime createdAt) {
    }
}
