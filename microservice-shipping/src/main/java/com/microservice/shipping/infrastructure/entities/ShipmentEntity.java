package com.microservice.shipping.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for Shipment persistence.
 */
@Entity
@Table(name = "shipments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentEntity {

    @Id
    @Column(name = "id_shipment")
    private UUID id;

    @Column(name = "tracking_number", unique = true, nullable = false)
    private String trackingNumber;

    @Column(name = "external_order_id", nullable = false)
    private UUID externalOrderId;

    @Column(name = "external_store_id")
    private UUID externalStoreId;

    @Column(name = "external_customer_id")
    private UUID externalCustomerId;

    @Column(name = "carrier_id", nullable = false)
    private UUID carrierId;

    @Column(name = "shipping_method_id")
    private UUID shippingMethodId;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "shipping_method_name")
    private String shippingMethodName;

    // Origin Address
    @Column(name = "origin_street")
    private String originStreet;

    @Column(name = "origin_city")
    private String originCity;

    @Column(name = "origin_state")
    private String originState;

    @Column(name = "origin_zip_code")
    private String originZipCode;

    @Column(name = "origin_country")
    private String originCountry;

    // Destination Address
    @Column(name = "destination_street")
    private String destinationStreet;

    @Column(name = "destination_city")
    private String destinationCity;

    @Column(name = "destination_state")
    private String destinationState;

    @Column(name = "destination_zip_code")
    private String destinationZipCode;

    @Column(name = "destination_country")
    private String destinationCountry;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "shipping_cost", precision = 19, scale = 4)
    private BigDecimal shippingCost;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "notes")
    private String notes;

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShipmentItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TrackingEventEntity> trackingEvents = new ArrayList<>();
}
