package com.microservice.user.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA para direcciones
 */
@Entity
@Table(name = "address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "external_user_id", nullable = false, columnDefinition = "uuid")
    private UUID externalUserId;
    
    @Column(name = "label", length = 50)
    private String label;
    
    @Column(name = "street_address", columnDefinition = "text", nullable = false)
    private String streetAddress;
    
    @Column(name = "city", length = 100, nullable = false)
    private String city;
    
    @Column(name = "state", length = 100)
    private String state;
    
    @Column(name = "postal_code", length = 50)
    private String postalCode;
    
    @Column(name = "country", length = 100, nullable = false)
    private String country;
    
    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean isDefault = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
