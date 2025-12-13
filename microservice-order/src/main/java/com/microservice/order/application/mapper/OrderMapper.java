package com.microservice.order.application.mapper;

import com.microservice.order.application.dto.request.CreateOrderRequest;
import com.microservice.order.application.dto.response.OrderResponse;
import com.microservice.order.domain.models.*;
import com.microservice.order.domain.models.records.Address;
import com.microservice.order.domain.port.in.CreateOrderPort;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs.
 */
@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getExternalUserProfileId())
                .storeId(order.getExternalStoreId())
                .status(order.getStatus())
                .currency(order.getCurrency())
                .shippingAddress(toAddressResponse(order.getShippingAddress()))
                .billingAddress(toAddressResponse(order.getBillingAddress()))
                .items(order.getItems().stream().map(this::toItemResponse).toList())
                .coupons(order.getCoupons().stream().map(this::toCouponResponse).toList())
                .subtotal(order.getSubtotal().amount())
                .discountTotal(order.getDiscountTotal().amount())
                .taxTotal(order.getTaxTotal().amount())
                .shippingTotal(order.getShippingTotal() != null ? order.getShippingTotal().amount() : null)
                .grandTotal(order.getGrandTotal().amount())
                .customerNotes(order.getCustomerNotes())
                .confirmedAt(order.getConfirmedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancelledAt(order.getCancelledAt())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderResponse.AddressResponse toAddressResponse(Address address) {
        if (address == null)
            return null;
        return OrderResponse.AddressResponse.builder()
                .street(address.street())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode())
                .country(address.country())
                .fullAddress(address.getFullAddress())
                .build();
    }

    private OrderResponse.OrderItemResponse toItemResponse(OrderItem item) {
        return OrderResponse.OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getExternalProductId())
                .productName(item.getProductName())
                .variantName(item.getVariantName())
                .sku(item.getSku())
                .imageUrl(item.getImageUrl())
                .unitPrice(item.getUnitPrice().amount())
                .quantity(item.getQuantity())
                .discountAmount(item.getDiscountAmount().amount())
                .taxAmount(item.getTaxAmount().amount())
                .lineTotal(item.getLineTotal().amount())
                .status(item.getStatus())
                .build();
    }

    private OrderResponse.OrderCouponResponse toCouponResponse(OrderCoupon coupon) {
        return OrderResponse.OrderCouponResponse.builder()
                .id(coupon.getId())
                .couponCode(coupon.getCouponCode())
                .discountAmount(coupon.getDiscountAmount().amount())
                .createdAt(coupon.getCreatedAt())
                .build();
    }

    public CreateOrderPort.CreateOrderCommand toCommand(CreateOrderRequest request, String ipAddress,
            String userAgent) {
        return new CreateOrderPort.CreateOrderCommand(
                request.customerId(),
                request.storeId(),
                request.currency(),
                request.shippingAddress().street(),
                request.shippingAddress().city(),
                request.shippingAddress().state(),
                request.shippingAddress().zipCode(),
                request.shippingAddress().country(),
                request.billingAddress() != null ? request.billingAddress().street() : null,
                request.billingAddress() != null ? request.billingAddress().city() : null,
                request.billingAddress() != null ? request.billingAddress().state() : null,
                request.billingAddress() != null ? request.billingAddress().zipCode() : null,
                request.billingAddress() != null ? request.billingAddress().country() : null,
                request.items().stream().map(this::toItemCommand).toList(),
                request.customerNotes(),
                ipAddress,
                userAgent);
    }

    private CreateOrderPort.OrderItemCommand toItemCommand(CreateOrderRequest.OrderItemRequest item) {
        return new CreateOrderPort.OrderItemCommand(
                item.productId(),
                item.productName(),
                item.variantName(),
                item.sku(),
                item.imageUrl(),
                item.unitPrice(),
                item.quantity());
    }
}
