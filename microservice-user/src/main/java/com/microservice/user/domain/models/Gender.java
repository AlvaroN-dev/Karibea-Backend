package com.microservice.user.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de catálogo - Género
 */
public class Gender {
    
    private final UUID id;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    private Gender(UUID id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static Gender create(String name) {
        Objects.requireNonNull(name, "Gender name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Gender name cannot be blank");
        }
        
        LocalDateTime now = LocalDateTime.now();
        return new Gender(UUID.randomUUID(), name.trim(), now, now);
    }
    
    public static Gender reconstitute(UUID id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Gender(id, name, createdAt, updatedAt);
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gender gender = (Gender) o;
        return Objects.equals(id, gender.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
