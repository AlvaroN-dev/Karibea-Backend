package com.microservice.user.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA para preferencias del usuario
 */
@Entity
@Table(name = "user_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesEntity {
    
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "external_user_id", nullable = false, unique = true, columnDefinition = "uuid")
    private UUID externalUserId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lenguaje_id")
    private LanguageEntity language;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;
    
    @Column(name = "notification_email", nullable = false)
    @Builder.Default
    private boolean notificationEmail = true;
    
    @Column(name = "notification_push", nullable = false)
    @Builder.Default
    private boolean notificationPush = true;
    
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
