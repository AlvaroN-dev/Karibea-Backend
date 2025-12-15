package com.microservice.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing address.
 * All fields are optional - only provided fields will be updated.
 */
@Schema(description = "Request to update an existing address")
public class UpdateAddressRequest {

    @Schema(
        description = "Label for the address (e.g., 'Home', 'Work', 'Office')",
        example = "Office",
        maxLength = 50
    )
    @Size(max = 50, message = "Label must be less than 50 characters")
    private String label;

    @Schema(
        description = "Full street address including number and apartment",
        example = "456 Business Ave, Suite 100"
    )
    private String streetAddress;

    @Schema(
        description = "City name",
        example = "Los Angeles",
        maxLength = 100
    )
    @Size(max = 100, message = "City must be less than 100 characters")
    private String city;

    @Schema(
        description = "State or province name",
        example = "CA",
        maxLength = 100
    )
    @Size(max = 100, message = "State must be less than 100 characters")
    private String state;

    @Schema(
        description = "Postal or ZIP code",
        example = "90001",
        maxLength = 50
    )
    @Size(max = 50, message = "Postal code must be less than 50 characters")
    private String postalCode;

    @Schema(
        description = "Country name",
        example = "United States",
        maxLength = 100
    )
    @Size(max = 100, message = "Country must be less than 100 characters")
    private String country;

    // Constructors
    public UpdateAddressRequest() {
    }

    public UpdateAddressRequest(String label, String streetAddress, String city,
                                String state, String postalCode, String country) {
        this.label = label;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    // Getters and Setters
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
}
