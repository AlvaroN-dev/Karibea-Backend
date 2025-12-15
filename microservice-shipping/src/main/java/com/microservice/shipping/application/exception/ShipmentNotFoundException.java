package com.microservice.shipping.application.exception;

import java.util.UUID;

/**
 * Exception thrown when a shipment is not found.
 */
public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(String message) {
        super(message);
    }

    public static ShipmentNotFoundException withId(UUID id) {
        return new ShipmentNotFoundException("Shipment not found with id: " + id);
    }

    public static ShipmentNotFoundException withTrackingNumber(String trackingNumber) {
        return new ShipmentNotFoundException("Shipment not found with tracking number: " + trackingNumber);
    }
}
