package com.microservice.shipping.domain.events;

import com.microservice.shipping.domain.models.Shipment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event published when a new shipment is created.
 */
public class ShipmentCreatedEvent extends DomainEvent {

    private final String trackingNumber;
    private final UUID orderId;
    private final UUID carrierId;
    private final String carrierCode;
    private final BigDecimal shippingCost;

    private ShipmentCreatedEvent(Shipment shipment) {
        super(shipment.getId(), "ShipmentCreated");
        this.trackingNumber = shipment.getTrackingNumber();
        this.orderId = shipment.getExternalOrderId();
        this.carrierId = shipment.getCarrierId();
        this.carrierCode = shipment.getCarrierCode();
        this.shippingCost = shipment.getShippingCost() != null ? shipment.getShippingCost().amount() : null;
    }

    public static ShipmentCreatedEvent of(Shipment shipment) {
        return new ShipmentCreatedEvent(shipment);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCarrierId() {
        return carrierId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }
}
