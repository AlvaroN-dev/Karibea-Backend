package com.microservice.order.application.dto.response;

import com.microservice.order.domain.models.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Order.
 * Contains complete order information including enriched external entity data.
 */
@Builder
@Schema(description = "Complete order information")
public record OrderResponse(
        @Schema(description = "Order unique identifier", example = "550e8400-e29b-41d4-a716-446655440100")
        UUID id,

        @Schema(description = "Human-readable order number", example = "ORD-20241215-A1B2C3D4")
        String orderNumber,

        @Schema(description = "Customer information")
        CustomerInfoResponse customer,

        @Schema(description = "Store information")
        StoreInfoResponse store,

        @Schema(description = "Payment information (when confirmed)")
        PaymentInfoResponse payment,

        @Schema(description = "Shipment information (when shipped)")
        ShipmentInfoResponse shipment,

        @Schema(description = "Current order status", example = "PENDING")
        OrderStatusEnum status,

        @Schema(description = "Order currency", example = "USD")
        String currency,

        @Schema(description = "Shipping address")
        AddressResponse shippingAddress,

        @Schema(description = "Billing address")
        AddressResponse billingAddress,

        @Schema(description = "Order items")
        List<OrderItemResponse> items,

        @Schema(description = "Applied coupons")
        List<OrderCouponResponse> coupons,

        @Schema(description = "Order status history")
        List<OrderStatusHistoryResponse> statusHistory,

        @Schema(description = "Subtotal before discounts and taxes", example = "199.98")
        BigDecimal subtotal,

        @Schema(description = "Total discount amount", example = "20.00")
        BigDecimal discountTotal,

        @Schema(description = "Total tax amount", example = "15.99")
        BigDecimal taxTotal,

        @Schema(description = "Shipping cost", example = "9.99")
        BigDecimal shippingTotal,

        @Schema(description = "Grand total including all charges", example = "205.96")
        BigDecimal grandTotal,

        @Schema(description = "Customer notes for the order")
        String customerNotes,

        @Schema(description = "When the order was confirmed")
        LocalDateTime confirmedAt,

        @Schema(description = "When the order was shipped")
        LocalDateTime shippedAt,

        @Schema(description = "When the order was delivered")
        LocalDateTime deliveredAt,

        @Schema(description = "When the order was cancelled")
        LocalDateTime cancelledAt,

        @Schema(description = "Order creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
    @Builder
    @Schema(description = "Address information")
    public record AddressResponse(
            @Schema(description = "Street address", example = "123 Main Street")
            String street,

            @Schema(description = "City", example = "New York")
            String city,

            @Schema(description = "State/Province", example = "NY")
            String state,

            @Schema(description = "ZIP/Postal code", example = "10001")
            String zipCode,

            @Schema(description = "Country", example = "USA")
            String country,

            @Schema(description = "Full formatted address")
            String fullAddress
    ) {
    }

    @Builder
    @Schema(description = "Order item information")
    public record OrderItemResponse(
            @Schema(description = "Item unique identifier")
            UUID id,

            @Schema(description = "Product information")
            ProductInfoResponse product,

            @Schema(description = "Product name at time of order", example = "Wireless Headphones")
            String productName,

            @Schema(description = "Variant name", example = "Black")
            String variantName,

            @Schema(description = "SKU", example = "WH-001-BLK")
            String sku,

            @Schema(description = "Product image URL")
            String imageUrl,

            @Schema(description = "Unit price", example = "99.99")
            BigDecimal unitPrice,

            @Schema(description = "Quantity", example = "2")
            int quantity,

            @Schema(description = "Discount applied to this item", example = "0.00")
            BigDecimal discountAmount,

            @Schema(description = "Tax amount for this item", example = "0.00")
            BigDecimal taxAmount,

            @Schema(description = "Total line amount", example = "199.98")
            BigDecimal lineTotal,

            @Schema(description = "Item status", example = "ACTIVE")
            String status
    ) {
    }

    @Builder
    @Schema(description = "Applied coupon information")
    public record OrderCouponResponse(
            @Schema(description = "Coupon unique identifier")
            UUID id,

            @Schema(description = "Coupon code", example = "SAVE20")
            String couponCode,

            @Schema(description = "Discount amount", example = "20.00")
            BigDecimal discountAmount,

            @Schema(description = "When the coupon was applied")
            LocalDateTime createdAt
    ) {
    }

    @Builder
    @Schema(description = "Order status change history")
    public record OrderStatusHistoryResponse(
            @Schema(description = "History entry unique identifier")
            UUID id,

            @Schema(description = "Previous status", example = "PENDING")
            OrderStatusEnum previousStatus,

            @Schema(description = "New status", example = "CONFIRMED")
            OrderStatusEnum newStatus,

            @Schema(description = "Who made the change", example = "payment-service")
            String changedBy,

            @Schema(description = "Reason for the change")
            String reason,

            @Schema(description = "When the change occurred")
            LocalDateTime createdAt
    ) {
    }
}
