package com.microservice.shipping.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for TrackingEvent persistence.
 */
@Entity
@Table(name = "tracking_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEventEntity {

    @Id
    @Column(name = "id_tracking_event")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_shipment", nullable = false)
    private ShipmentEntity shipment;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
