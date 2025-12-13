package com.microservice.order.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for OrderCoupon persistence.
 */
@Entity
@Table(name = "order_coupons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCouponEntity {

    @Id
    @Column(name = "id_order_coupon")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order", nullable = false)
    private OrderEntity order;

    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @Column(name = "discount_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal discountAmount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
