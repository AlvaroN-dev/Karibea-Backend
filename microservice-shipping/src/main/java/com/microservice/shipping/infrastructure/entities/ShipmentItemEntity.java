package com.microservice.shipping.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for ShipmentItem persistence.
 */
@Entity
@Table(name = "shipment_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentItemEntity {

    @Id
    @Column(name = "id_shipment_item")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_shipment", nullable = false)
    private ShipmentEntity shipment;

    @Column(name = "external_order_item_id")
    private UUID externalOrderItemId;

    @Column(name = "external_product_id")
    private UUID externalProductId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku")
    private String sku;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
