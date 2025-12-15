package com.microservice.user.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de catálogo - Moneda
 */
public class Currency {
    
    private final UUID id;
    private final String name;
    private final String code;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    private Currency(UUID id, String name, String code, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static Currency create(String name, String code) {
        Objects.requireNonNull(name, "Currency name cannot be null");
        Objects.requireNonNull(code, "Currency code cannot be null");
        
        if (name.isBlank()) {
            throw new IllegalArgumentException("Currency name cannot be blank");
        }
        if (code.isBlank() || code.length() > 10) {
            throw new IllegalArgumentException("Currency code must be between 1 and 10 characters");
        }
        
        LocalDateTime now = LocalDateTime.now();
        return new Currency(UUID.randomUUID(), name.trim(), code.trim().toUpperCase(), now, now);
    }
    
    public static Currency reconstitute(UUID id, String name, String code, 
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Currency(id, name, code, createdAt, updatedAt);
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCode() {
        return code;
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
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
