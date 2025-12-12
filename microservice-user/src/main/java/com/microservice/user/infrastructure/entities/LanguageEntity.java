package com.microservice.user.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA para idiomas
 */
@Entity
@Table(name = "lenguaje")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageEntity {
    
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "name", length = 200, nullable = false)
    private String name;
    
    @Column(name = "code", length = 10, unique = true, nullable = false)
    private String code;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
