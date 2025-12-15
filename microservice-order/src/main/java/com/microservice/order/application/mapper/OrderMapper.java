package com.microservice.order.application.mapper;

import com.microservice.order.application.dto.request.CreateOrderRequest;
import com.microservice.order.application.dto.response.*;
import com.microservice.order.domain.models.*;
import com.microservice.order.domain.models.records.Address;
import com.microservice.order.domain.port.in.CreateOrderPort;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs.
 * 
 * This mapper handles the transformation of domain entities to response DTOs,
 * including the creation of enriched external entity information placeholders.
 * External entity data is included with ID references that can be resolved
 * by client-side data fetching or BFF (Backend For Frontend) patterns.
 */
@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customer(toCustomerInfo(order.getExternalUserProfileId()))
                .store(toStoreInfo(order.getExternalStoreId()))
                .payment(order.getExternalPaymentId() != null ? toPaymentInfo(order.getExternalPaymentId()) : null)
                .shipment(order.getExternalShipmentId() != null ? toShipmentInfo(order.getExternalShipmentId()) : null)
                .status(order.getStatus())
                .currency(order.getCurrency())
                .shippingAddress(toAddressResponse(order.getShippingAddress()))
                .billingAddress(toAddressResponse(order.getBillingAddress()))
                .items(order.getItems().stream().map(this::toItemResponse).toList())
                .coupons(order.getCoupons().stream().map(this::toCouponResponse).toList())
                .statusHistory(order.getStatusHistory().stream().map(this::toHistoryResponse).toList())
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

    private CustomerInfoResponse toCustomerInfo(java.util.UUID customerId) {
        // Returns customer info with ID reference.
        // In a BFF pattern, this would be enriched with actual customer data.
        // Client can fetch additional details using the customer ID if needed.
        return CustomerInfoResponse.withIdOnly(customerId);
    }

    private StoreInfoResponse toStoreInfo(java.util.UUID storeId) {
        // Returns store info with ID reference.
        // In a BFF pattern, this would be enriched with actual store data.
        return StoreInfoResponse.withIdOnly(storeId);
    }

    private PaymentInfoResponse toPaymentInfo(java.util.UUID paymentId) {
        // Returns payment info with ID reference.
        // Payment details are received via events and stored if needed.
        return PaymentInfoResponse.withIdOnly(paymentId);
    }

    private ShipmentInfoResponse toShipmentInfo(java.util.UUID shipmentId) {
        // Returns shipment info with ID reference.
        // Shipment details are received via events and stored if needed.
        return ShipmentInfoResponse.withIdOnly(shipmentId);
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
                .product(ProductInfoResponse.withIdOnly(item.getExternalProductId()))
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

    private OrderResponse.OrderStatusHistoryResponse toHistoryResponse(OrderStatusHistory history) {
        return OrderResponse.OrderStatusHistoryResponse.builder()
                .id(history.getId())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .changedBy(history.getChangedBy())
                .reason(history.getReason())
                .createdAt(history.getCreatedAt())
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
