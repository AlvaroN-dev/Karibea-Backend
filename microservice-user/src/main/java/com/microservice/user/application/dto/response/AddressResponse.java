package com.microservice.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for address information.
 */
@Schema(description = "Address information response")
public class AddressResponse {

    @Schema(description = "Unique address ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "External user ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID externalUserId;

    @Schema(description = "Label for the address", example = "Home")
    private String label;

    @Schema(description = "Street address", example = "123 Main Street, Apt 4B")
    private String streetAddress;

    @Schema(description = "City name", example = "New York")
    private String city;

    @Schema(description = "State or province", example = "NY")
    private String state;

    @Schema(description = "Postal code", example = "10001")
    private String postalCode;

    @Schema(description = "Country name", example = "United States")
    private String country;

    @Schema(description = "Formatted full address", example = "123 Main Street, Apt 4B, New York, NY 10001, United States")
    private String formattedAddress;

    @Schema(description = "Whether this is the default address", example = "true")
    private boolean isDefault;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-06-20T14:45:00")
    private LocalDateTime updatedAt;

    // Constructors
    public AddressResponse() {
    }

    public AddressResponse(UUID id, UUID externalUserId, String label, String streetAddress,
                           String city, String state, String postalCode, String country,
                           String formattedAddress, boolean isDefault,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.externalUserId = externalUserId;
        this.label = label;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.formattedAddress = formattedAddress;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getExternalUserId() { return externalUserId; }
    public void setExternalUserId(UUID externalUserId) { this.externalUserId = externalUserId; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getFormattedAddress() { return formattedAddress; }
    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
