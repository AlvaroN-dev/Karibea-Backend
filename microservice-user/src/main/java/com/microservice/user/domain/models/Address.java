package com.microservice.user.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio - Dirección del usuario
 */
public class Address {
    
    private final UUID id;
    private final UUID externalUserId;
    private String label;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean deleted;
    
    private Address(UUID id, UUID externalUserId, String label, String streetAddress,
                   String city, String state, String postalCode, String country,
                   boolean isDefault, LocalDateTime createdAt, LocalDateTime updatedAt,
                   LocalDateTime deletedAt, boolean deleted) {
        this.id = id;
        this.externalUserId = externalUserId;
        this.label = label;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.deleted = deleted;
    }
    
    /**
     * Factory method para crear una nueva dirección
     */
    public static Address create(UUID externalUserId, String label, String streetAddress,
                                 String city, String state, String postalCode, String country) {
        Objects.requireNonNull(externalUserId, "External user ID cannot be null");
        Objects.requireNonNull(streetAddress, "Street address cannot be null");
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");
        
        LocalDateTime now = LocalDateTime.now();
        return new Address(
            UUID.randomUUID(),
            externalUserId,
            label,
            streetAddress.trim(),
            city.trim(),
            state,
            postalCode,
            country.trim(),
            false,
            now,
            now,
            null,
            false
        );
    }
    
    /**
     * Factory method para reconstruir desde persistencia
     */
    public static Address reconstitute(UUID id, UUID externalUserId, String label,
                                       String streetAddress, String city, String state,
                                       String postalCode, String country, boolean isDefault,
                                       LocalDateTime createdAt, LocalDateTime updatedAt,
                                       LocalDateTime deletedAt, boolean deleted) {
        return new Address(id, externalUserId, label, streetAddress, city, state,
                          postalCode, country, isDefault, createdAt, updatedAt, deletedAt, deleted);
    }
    
    // Métodos de dominio
    
    public void update(String label, String streetAddress, String city,
                       String state, String postalCode, String country) {
        this.label = label;
        if (streetAddress != null && !streetAddress.isBlank()) {
            this.streetAddress = streetAddress.trim();
        }
        if (city != null && !city.isBlank()) {
            this.city = city.trim();
        }
        this.state = state;
        this.postalCode = postalCode;
        if (country != null && !country.isBlank()) {
            this.country = country.trim();
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeDefault() {
        this.isDefault = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(streetAddress);
        if (city != null) sb.append(", ").append(city);
        if (state != null) sb.append(", ").append(state);
        if (postalCode != null) sb.append(" ").append(postalCode);
        sb.append(", ").append(country);
        return sb.toString();
    }
    
    // Getters
    
    public UUID getId() {
        return id;
    }
    
    public UUID getExternalUserId() {
        return externalUserId;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getState() {
        return state;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
