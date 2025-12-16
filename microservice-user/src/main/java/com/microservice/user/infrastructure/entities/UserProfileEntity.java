package com.microservice.user.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA para perfiles de usuario
 */
@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {
    
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "external_user_id", nullable = false, unique = true, columnDefinition = "uuid")
    private UUID externalUserId;
    
    @Column(name = "first_name", length = 255)
    private String firstName;
    
    @Column(name = "last_name", length = 255)
    private String lastName;
    
    @Column(name = "middle_name", length = 255)
    private String middleName;
    
    @Column(name = "second_lastname", length = 255)
    private String secondLastname;
    
    @Column(name = "phone", length = 100)
    private String phone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private GenderEntity gender;
    
    @Column(name = "avatar_url", columnDefinition = "text")
    private String avatarUrl;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
