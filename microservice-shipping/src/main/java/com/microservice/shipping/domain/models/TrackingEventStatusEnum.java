package com.microservice.shipping.domain.models;

/**
 * Enum representing tracking event status.
 * PURE DOMAIN - No framework dependencies.
 */
public enum TrackingEventStatusEnum {
    LABEL_CREATED,
    PICKED_UP,
    ARRIVED_AT_FACILITY,
    DEPARTED_FACILITY,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERY_ATTEMPTED,
    DELIVERED,
    EXCEPTION,
    RETURNED_TO_SENDER
}
