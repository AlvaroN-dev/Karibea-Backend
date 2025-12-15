package com.microservice.order.application.dto.response;

import lombok.Builder;

import java.util.UUID;

/**
 * Response DTO for Customer (User Profile) information.
 * Contains enriched data from external user-profiles microservice context.
 */
@Builder
public record CustomerInfoResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
    /**
     * Creates a CustomerInfoResponse with only the ID (when external data is not available).
     */
    public static CustomerInfoResponse withIdOnly(UUID id) {
        return CustomerInfoResponse.builder()
                .id(id)
                .build();
    }

    /**
     * Get full name combining first and last name.
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName);
        }
        if (lastName != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(lastName);
        }
        return sb.toString();
    }
}
