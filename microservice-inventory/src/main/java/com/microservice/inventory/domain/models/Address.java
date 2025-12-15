package com.microservice.inventory.domain.models;

import java.util.Objects;

/**
 * Value Object representing an address.
 */
public final class Address {

    private final String street;
    private final String city;
    private final String country;

    private Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public static Address of(String street, String city, String country) {
        return new Address(street, city, country);
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s", street, city, country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(city, address.city) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, country);
    }

    @Override
    public String toString() {
        return getFullAddress();
    }
}
