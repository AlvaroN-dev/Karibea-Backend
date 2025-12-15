package com.microservice.shipping.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO containing enriched store information.
 * Represents external store data (fulfillment location) referenced by the shipment.
 */
@Schema(description = "Store information associated with the shipment")
public class StoreInfoResponse {

    @Schema(
            description = "Unique identifier of the store",
            example = "550e8400-e29b-41d4-a716-446655440002"
    )
    private UUID id;

    @Schema(
            description = "Name of the store",
            example = "Electronics Hub - Downtown"
    )
    private String name;

    @Schema(
            description = "Store email address",
            example = "downtown@electronicshub.com"
    )
    private String email;

    @Schema(
            description = "Store phone number",
            example = "+1-555-123-4567"
    )
    private String phone;

    @Schema(
            description = "Store logo URL",
            example = "https://cdn.karibea.com/stores/logo-electronics-hub.png"
    )
    private String logoUrl;

    // Constructors
    public StoreInfoResponse() {
    }

    public StoreInfoResponse(UUID id, String name, String email, String phone, String logoUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.logoUrl = logoUrl;
    }

    // Static factory for creating with just ID
    public static StoreInfoResponse withId(UUID id) {
        StoreInfoResponse response = new StoreInfoResponse();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
