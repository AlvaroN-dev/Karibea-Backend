package com.microservice.shipping.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO containing enriched customer information.
 * Represents external customer data referenced by the shipment.
 */
@Schema(description = "Customer information associated with the shipment")
public class CustomerInfoResponse {

    @Schema(
            description = "Unique identifier of the customer",
            example = "550e8400-e29b-41d4-a716-446655440003"
    )
    private UUID id;

    @Schema(
            description = "Customer's first name",
            example = "John"
    )
    private String firstName;

    @Schema(
            description = "Customer's last name",
            example = "Doe"
    )
    private String lastName;

    @Schema(
            description = "Customer's email address",
            example = "john.doe@example.com"
    )
    private String email;

    @Schema(
            description = "Customer's phone number",
            example = "+1-555-987-6543"
    )
    private String phone;

    // Constructors
    public CustomerInfoResponse() {
    }

    public CustomerInfoResponse(UUID id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // Static factory for creating with just ID
    public static CustomerInfoResponse withId(UUID id) {
        CustomerInfoResponse response = new CustomerInfoResponse();
        response.setId(id);
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
