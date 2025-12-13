package com.microservice.order.application.exception;

/**
 * Exception thrown when an order is not found.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException withId(java.util.UUID id) {
        return new OrderNotFoundException("Order not found with id: " + id);
    }

    public static OrderNotFoundException withOrderNumber(String orderNumber) {
        return new OrderNotFoundException("Order not found with number: " + orderNumber);
    }
}
