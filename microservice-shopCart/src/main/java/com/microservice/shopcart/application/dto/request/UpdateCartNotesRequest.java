package com.microservice.shopcart.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating shopping cart notes.
 */
@Schema(description = "Request to update shopping cart notes")
public class UpdateCartNotesRequest {

    @Schema(description = "Notes or special instructions for the cart", 
            example = "Please gift wrap the items",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 1000)
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    // Constructors
    public UpdateCartNotesRequest() {
    }

    public UpdateCartNotesRequest(String notes) {
        this.notes = notes;
    }

    // Getters and Setters
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
