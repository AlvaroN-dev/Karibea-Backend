package com.microservice.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO for currency catalog item.
 */
@Schema(description = "Currency catalog information")
public class CurrencyResponse {

    @Schema(description = "Unique currency ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Currency name", example = "United States Dollar")
    private String name;

    @Schema(description = "Currency ISO code", example = "USD")
    private String code;

    // Constructors
    public CurrencyResponse() {
    }

    public CurrencyResponse(UUID id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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
}
