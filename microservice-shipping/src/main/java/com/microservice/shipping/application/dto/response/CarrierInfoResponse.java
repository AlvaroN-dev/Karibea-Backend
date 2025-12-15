package com.microservice.shipping.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO containing enriched carrier information.
 * Represents carrier details for the shipment.
 */
@Schema(description = "Carrier information for the shipment")
public class CarrierInfoResponse {

    @Schema(
            description = "Unique identifier of the carrier",
            example = "550e8400-e29b-41d4-a716-446655440004"
    )
    private UUID id;

    @Schema(
            description = "Name of the carrier",
            example = "FedEx Express"
    )
    private String name;

    @Schema(
            description = "Carrier code identifier",
            example = "FEDEX"
    )
    private String code;

    @Schema(
            description = "URL template for tracking",
            example = "https://www.fedex.com/track?trackingNumber={trackingNumber}"
    )
    private String trackingUrlTemplate;

    @Schema(
            description = "Whether the carrier is active",
            example = "true"
    )
    private Boolean isActive;

    @Schema(
            description = "Carrier logo URL",
            example = "https://cdn.karibea.com/carriers/fedex-logo.png"
    )
    private String logoUrl;

    // Constructors
    public CarrierInfoResponse() {
    }

    public CarrierInfoResponse(UUID id, String name, String code, String trackingUrlTemplate, 
                                Boolean isActive, String logoUrl) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.trackingUrlTemplate = trackingUrlTemplate;
        this.isActive = isActive;
        this.logoUrl = logoUrl;
    }

    // Static factory for creating with just ID
    public static CarrierInfoResponse withId(UUID id) {
        CarrierInfoResponse response = new CarrierInfoResponse();
        response.setId(id);
        return response;
    }

    /**
     * Generates the tracking URL with the provided tracking number.
     */
    public String getTrackingUrl(String trackingNumber) {
        if (trackingUrlTemplate == null || trackingNumber == null) {
            return null;
        }
        return trackingUrlTemplate.replace("{trackingNumber}", trackingNumber);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTrackingUrlTemplate() {
        return trackingUrlTemplate;
    }

    public void setTrackingUrlTemplate(String trackingUrlTemplate) {
        this.trackingUrlTemplate = trackingUrlTemplate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
