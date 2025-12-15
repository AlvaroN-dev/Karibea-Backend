package com.microservice.user.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de catálogo - Idioma
 */
public class Language {
    
    private final UUID id;
    private final String name;
    private final String code;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    private Language(UUID id, String name, String code, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static Language create(String name, String code) {
        Objects.requireNonNull(name, "Language name cannot be null");
        Objects.requireNonNull(code, "Language code cannot be null");
        
        if (name.isBlank()) {
            throw new IllegalArgumentException("Language name cannot be blank");
        }
        if (code.isBlank() || code.length() > 10) {
            throw new IllegalArgumentException("Language code must be between 1 and 10 characters");
        }
        
        LocalDateTime now = LocalDateTime.now();
        return new Language(UUID.randomUUID(), name.trim(), code.trim().toLowerCase(), now, now);
    }
    
    public static Language reconstitute(UUID id, String name, String code, 
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Language(id, name, code, createdAt, updatedAt);
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
        Language language = (Language) o;
        return Objects.equals(id, language.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
