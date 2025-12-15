package com.microservice.shipping.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Carrier persistence.
 */
@Entity
@Table(name = "carriers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrierEntity {

    @Id
    @Column(name = "id_carrier")
    private UUID id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tracking_url_template")
    private String trackingUrlTemplate;

    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
