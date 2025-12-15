package com.microservice.order.domain.exceptions;

/**
 * Base exception for all domain-level exceptions.
 */
public class OrderDomainException extends RuntimeException {

    private final String code;

    public OrderDomainException(String message) {
        super(message);
        this.code = "DOMAIN_ERROR";
    }

    public OrderDomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
