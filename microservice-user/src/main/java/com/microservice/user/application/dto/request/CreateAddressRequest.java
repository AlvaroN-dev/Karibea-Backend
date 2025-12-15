package com.microservice.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating a user address.
 * Contains address information linked to a user's external ID.
 */
@Schema(description = "Request to create a new address for a user")
public class CreateAddressRequest {

    @Schema(
        description = "External user ID from the identity service",
        example = "550e8400-e29b-41d4-a716-446655440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "External user ID is required")
    private UUID externalUserId;

    @Schema(
        description = "Label for the address (e.g., 'Home', 'Work', 'Office')",
        example = "Home",
        maxLength = 50
    )
    @Size(max = 50, message = "Label must be less than 50 characters")
    private String label;

    @Schema(
        description = "Full street address including number and apartment",
        example = "123 Main Street, Apt 4B",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Street address is required")
    private String streetAddress;

    @Schema(
        description = "City name",
        example = "New York",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be less than 100 characters")
    private String city;

    @Schema(
        description = "State or province name",
        example = "NY",
        maxLength = 100
    )
    @Size(max = 100, message = "State must be less than 100 characters")
    private String state;

    @Schema(
        description = "Postal or ZIP code",
        example = "10001",
        maxLength = 50
    )
    @Size(max = 50, message = "Postal code must be less than 50 characters")
    private String postalCode;

    @Schema(
        description = "Country name",
        example = "United States",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be less than 100 characters")
    private String country;

    @Schema(
        description = "Whether this is the default address for the user",
        example = "true"
    )
    private boolean isDefault;

    // Constructors
    public CreateAddressRequest() {
    }

    public CreateAddressRequest(UUID externalUserId, String label, String streetAddress,
                                String city, String state, String postalCode,
                                String country, boolean isDefault) {
        this.externalUserId = externalUserId;
        this.label = label;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    // Getters and Setters
    public UUID getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(UUID externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
