package com.microservice.order.domain.models.records;

import java.util.Objects;

/**
 * Value Object representing a physical address.
 */
public record Address(
        String street,
        String city,
        String state,
        String zipCode,
        String country) {
    public Address {
        Objects.requireNonNull(street, "Street cannot be null");
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");

        if (street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be blank");
        }
        if (city.isBlank()) {
            throw new IllegalArgumentException("City cannot be blank");
        }
        if (country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be blank");
        }
    }

    public static Address of(String street, String city, String state, String zipCode, String country) {
        return new Address(street, city, state, zipCode, country);
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", ");
        sb.append(city);
        if (state != null && !state.isBlank()) {
            sb.append(", ").append(state);
        }
        if (zipCode != null && !zipCode.isBlank()) {
            sb.append(" ").append(zipCode);
        }
        sb.append(", ").append(country);
        return sb.toString();
    }
}
