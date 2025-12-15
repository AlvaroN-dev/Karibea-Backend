package com.microservice.shipping.domain.exceptions;

/**
 * Base exception for domain-level exceptions.
 */
public class ShipmentDomainException extends RuntimeException {

    private final String code;

    public ShipmentDomainException(String message) {
        super(message);
        this.code = "DOMAIN_ERROR";
    }

    public ShipmentDomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
