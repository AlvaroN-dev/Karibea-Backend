package com.microservice.order.infrastructure.adapters;

import com.microservice.order.domain.models.*;
import com.microservice.order.domain.port.out.OrderRepositoryPort;
import com.microservice.order.infrastructure.entities.*;
import com.microservice.order.infrastructure.repositories.JpaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing OrderRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaRepository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findByIdWithItems(id).map(this::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber).map(this::toDomain);
    }

    @Override
    public Page<Order> findByCustomerId(UUID customerId, Pageable pageable) {
        return jpaRepository.findByExternalUserProfileId(customerId, pageable).map(this::toDomain);
    }

    @Override
    public Page<Order> findByStoreId(UUID storeId, Pageable pageable) {
        return jpaRepository.findByExternalStoreId(storeId, pageable).map(this::toDomain);
    }

    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return jpaRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    // ========== Entity to Domain Mapping ==========

    private Order toDomain(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .externalUserProfileId(entity.getExternalUserProfileId())
                .externalStoreId(entity.getExternalStoreId())
                .externalPaymentId(entity.getExternalPaymentId())
                .externalShipmentId(entity.getExternalShipmentId())
                .status(OrderStatusEnum.valueOf(entity.getStatus()))
                .currency(entity.getCurrency())
                .shippingAddress(toAddress(entity.getShippingStreet(), entity.getShippingCity(),
                        entity.getShippingState(), entity.getShippingZipCode(), entity.getShippingCountry()))
                .billingAddress(toAddress(entity.getBillingStreet(), entity.getBillingCity(),
                        entity.getBillingState(), entity.getBillingZipCode(), entity.getBillingCountry()))
                .subtotal(Money.of(entity.getSubtotal()))
                .discountTotal(Money.of(entity.getDiscountTotal()))
                .taxTotal(Money.of(entity.getTaxTotal()))
                .shippingTotal(entity.getShippingTotal() != null ? Money.of(entity.getShippingTotal()) : Money.zero())
                .grandTotal(Money.of(entity.getGrandTotal()))
                .notes(entity.getNotes())
                .customerNotes(entity.getCustomerNotes())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .confirmedAt(entity.getConfirmedAt())
                .shippedAt(entity.getShippedAt())
                .deliveredAt(entity.getDeliveredAt())
                .cancelledAt(entity.getCancelledAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .items(entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList()))
                .coupons(entity.getCoupons().stream().map(this::toCouponDomain).collect(Collectors.toList()))
                .statusHistory(
                        entity.getStatusHistory().stream().map(this::toHistoryDomain).collect(Collectors.toList()))
                .build();
    }

    private Address toAddress(String street, String city, String state, String zipCode, String country) {
        if (street == null || city == null || country == null)
            return null;
        return Address.of(street, city, state, zipCode, country);
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        return OrderItem.builder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .externalProductId(entity.getExternalProductId())
                .productName(entity.getProductName())
                .variantName(entity.getVariantName())
                .sku(entity.getSku())
                .imageUrl(entity.getImageUrl())
                .unitPrice(Money.of(entity.getUnitPrice()))
                .quantity(entity.getQuantity())
                .discountAmount(
                        entity.getDiscountAmount() != null ? Money.of(entity.getDiscountAmount()) : Money.zero())
                .taxAmount(entity.getTaxAmount() != null ? Money.of(entity.getTaxAmount()) : Money.zero())
                .lineTotal(Money.of(entity.getLineTotal()))
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private OrderCoupon toCouponDomain(OrderCouponEntity entity) {
        return OrderCoupon.builder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .couponCode(entity.getCouponCode())
                .discountAmount(Money.of(entity.getDiscountAmount()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private OrderStatusHistory toHistoryDomain(OrderStatusHistoryEntity entity) {
        return OrderStatusHistory.builder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .previousStatus(
                        entity.getPreviousStatus() != null ? OrderStatusEnum.valueOf(entity.getPreviousStatus()) : null)
                .newStatus(OrderStatusEnum.valueOf(entity.getNewStatus()))
                .changedBy(entity.getChangedBy())
                .reason(entity.getReason())
                .metadata(entity.getMetadata())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // ========== Domain to Entity Mapping ==========

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .externalUserProfileId(order.getExternalUserProfileId())
                .externalStoreId(order.getExternalStoreId())
                .externalPaymentId(order.getExternalPaymentId())
                .externalShipmentId(order.getExternalShipmentId())
                .status(order.getStatus().name())
                .currency(order.getCurrency())
                .shippingStreet(order.getShippingAddress() != null ? order.getShippingAddress().street() : null)
                .shippingCity(order.getShippingAddress() != null ? order.getShippingAddress().city() : null)
                .shippingState(order.getShippingAddress() != null ? order.getShippingAddress().state() : null)
                .shippingZipCode(order.getShippingAddress() != null ? order.getShippingAddress().zipCode() : null)
                .shippingCountry(order.getShippingAddress() != null ? order.getShippingAddress().country() : null)
                .billingStreet(order.getBillingAddress() != null ? order.getBillingAddress().street() : null)
                .billingCity(order.getBillingAddress() != null ? order.getBillingAddress().city() : null)
                .billingState(order.getBillingAddress() != null ? order.getBillingAddress().state() : null)
                .billingZipCode(order.getBillingAddress() != null ? order.getBillingAddress().zipCode() : null)
                .billingCountry(order.getBillingAddress() != null ? order.getBillingAddress().country() : null)
                .subtotal(order.getSubtotal().amount())
                .discountTotal(order.getDiscountTotal().amount())
                .taxTotal(order.getTaxTotal().amount())
                .shippingTotal(order.getShippingTotal() != null ? order.getShippingTotal().amount() : null)
                .grandTotal(order.getGrandTotal().amount())
                .notes(order.getNotes())
                .customerNotes(order.getCustomerNotes())
                .ipAddress(order.getIpAddress())
                .userAgent(order.getUserAgent())
                .confirmedAt(order.getConfirmedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancelledAt(order.getCancelledAt())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        // Map items
        order.getItems().forEach(item -> {
            OrderItemEntity itemEntity = toItemEntity(item, entity);
            entity.getItems().add(itemEntity);
        });

        // Map coupons
        order.getCoupons().forEach(coupon -> {
            OrderCouponEntity couponEntity = toCouponEntity(coupon, entity);
            entity.getCoupons().add(couponEntity);
        });

        // Map status history
        order.getStatusHistory().forEach(history -> {
            OrderStatusHistoryEntity historyEntity = toHistoryEntity(history, entity);
            entity.getStatusHistory().add(historyEntity);
        });

        return entity;
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity order) {
        return OrderItemEntity.builder()
                .id(item.getId())
                .order(order)
                .externalProductId(item.getExternalProductId())
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
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private OrderCouponEntity toCouponEntity(OrderCoupon coupon, OrderEntity order) {
        return OrderCouponEntity.builder()
                .id(coupon.getId())
                .order(order)
                .couponCode(coupon.getCouponCode())
                .discountAmount(coupon.getDiscountAmount().amount())
                .createdAt(coupon.getCreatedAt())
                .build();
    }

    private OrderStatusHistoryEntity toHistoryEntity(OrderStatusHistory history, OrderEntity order) {
        return OrderStatusHistoryEntity.builder()
                .id(history.getId())
                .order(order)
                .previousStatus(history.getPreviousStatus() != null ? history.getPreviousStatus().name() : null)
                .newStatus(history.getNewStatus().name())
                .changedBy(history.getChangedBy())
                .reason(history.getReason())
                .metadata(history.getMetadata())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
