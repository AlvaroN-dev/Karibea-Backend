package com.microservice.shipping.domain.models;

import com.microservice.shipping.domain.events.*;
import com.microservice.shipping.domain.exceptions.InvalidShipmentStateTransitionException;
import com.microservice.shipping.domain.exceptions.ShipmentInvariantViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Shipment Aggregate Root - the main entry point for all Shipment operations.
 * PURE DOMAIN - No framework dependencies.
 */
public class Shipment {

    private UUID id;
    private String trackingNumber;

    // External references (not owned by this service)
    private UUID externalOrderId;
    private UUID externalStoreId;
    private UUID externalCustomerId;

    // Carrier and method
    private UUID carrierId;
    private UUID shippingMethodId;
    private String carrierCode;
    private String carrierName;
    private String shippingMethodName;

    // Addresses
    private Address originAddress;
    private Address destinationAddress;

    // Status and details
    private ShipmentStatusEnum status;
    private Money shippingCost;
    private Double weightKg;
    private String dimensions;
    private String notes;

    // Timestamps
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Child entities
    private List<ShipmentItem> items;
    private List<TrackingEvent> trackingEvents;

    // Domain events (transient)
    private transient List<DomainEvent> domainEvents;

    private Shipment() {
        this.id = UUID.randomUUID();
        this.status = ShipmentStatusEnum.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.trackingEvents = new ArrayList<>();
        this.domainEvents = new ArrayList<>();
    }

    /**
     * Factory method to create a new Shipment.
     */
    public static Shipment create(
            UUID externalOrderId,
            UUID externalStoreId,
            UUID externalCustomerId,
            UUID carrierId,
            UUID shippingMethodId,
            String carrierCode,
            String carrierName,
            String shippingMethodName,
            Address originAddress,
            Address destinationAddress,
            Money shippingCost,
            List<ShipmentItem> items) {

        if (externalOrderId == null) {
            throw new ShipmentInvariantViolationException("Order ID is required");
        }
        if (carrierId == null) {
            throw new ShipmentInvariantViolationException("Carrier ID is required");
        }
        if (items == null || items.isEmpty()) {
            throw new ShipmentInvariantViolationException("Shipment must have at least one item");
        }
        if (destinationAddress == null) {
            throw new ShipmentInvariantViolationException("Destination address is required");
        }

        Shipment shipment = new Shipment();
        shipment.externalOrderId = externalOrderId;
        shipment.externalStoreId = externalStoreId;
        shipment.externalCustomerId = externalCustomerId;
        shipment.carrierId = carrierId;
        shipment.shippingMethodId = shippingMethodId;
        shipment.carrierCode = carrierCode;
        shipment.carrierName = carrierName;
        shipment.shippingMethodName = shippingMethodName;
        shipment.originAddress = originAddress;
        shipment.destinationAddress = destinationAddress;
        shipment.shippingCost = shippingCost;
        shipment.trackingNumber = generateTrackingNumber(carrierCode);

        for (ShipmentItem item : items) {
            shipment.addItem(item);
        }

        // Register domain event
        shipment.registerEvent(ShipmentCreatedEvent.of(shipment));

        return shipment;
    }

    private static String generateTrackingNumber(String carrierCode) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return (carrierCode != null ? carrierCode : "SHP") + "-" + datePart + "-" + randomPart;
    }

    // ========== Item Management ==========

    public void addItem(ShipmentItem item) {
        if (status.isFinalState()) {
            throw new ShipmentInvariantViolationException("Cannot add items to a finalized shipment");
        }
        item.setShipmentId(this.id);
        this.items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Tracking Events ==========

    public void addTrackingEvent(TrackingEvent event) {
        event.setShipmentId(this.id);
        this.trackingEvents.add(event);
        this.updatedAt = LocalDateTime.now();

        registerEvent(TrackingEventAddedEvent.of(this.id, event));
    }

    // ========== Status Transitions ==========

    public void confirm(String confirmedBy) {
        validateTransition(ShipmentStatusEnum.CONFIRMED);
        ShipmentStatusEnum previousStatus = this.status;
        this.status = ShipmentStatusEnum.CONFIRMED;
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.LABEL_CREATED,
                originAddress != null ? originAddress.city() : null,
                "Shipment label created",
                LocalDateTime.now()));

        registerEvent(ShipmentStatusChangedEvent.of(this.id, previousStatus, this.status, "Shipment confirmed"));
    }

    public void pickUp(String pickedUpBy) {
        validateTransition(ShipmentStatusEnum.PICKED_UP);

        this.status = ShipmentStatusEnum.PICKED_UP;
        this.pickedUpAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.PICKED_UP,
                originAddress != null ? originAddress.city() : null,
                "Package picked up by carrier",
                LocalDateTime.now()));

        registerEvent(ShipmentPickedUpEvent.of(this.id, this.pickedUpAt));
    }

    public void startTransit(String location) {
        validateTransition(ShipmentStatusEnum.IN_TRANSIT);
        ShipmentStatusEnum previousStatus = this.status;
        this.status = ShipmentStatusEnum.IN_TRANSIT;
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.IN_TRANSIT,
                location,
                "Package in transit",
                LocalDateTime.now()));

        registerEvent(ShipmentStatusChangedEvent.of(this.id, previousStatus, this.status, "In transit"));
    }

    public void outForDelivery(String location) {
        validateTransition(ShipmentStatusEnum.OUT_FOR_DELIVERY);
        ShipmentStatusEnum previousStatus = this.status;
        this.status = ShipmentStatusEnum.OUT_FOR_DELIVERY;
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.OUT_FOR_DELIVERY,
                location,
                "Package out for delivery",
                LocalDateTime.now()));

        registerEvent(ShipmentStatusChangedEvent.of(this.id, previousStatus, this.status, "Out for delivery"));
    }

    public void deliver(String deliveredBy, String location) {
        validateTransition(ShipmentStatusEnum.DELIVERED);

        this.status = ShipmentStatusEnum.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.DELIVERED,
                location != null ? location : destinationAddress.city(),
                "Package delivered",
                LocalDateTime.now()));

        registerEvent(ShipmentDeliveredEvent.of(this.id, this.deliveredAt));
    }

    public void failDelivery(String reason, String location) {
        validateTransition(ShipmentStatusEnum.FAILED_DELIVERY);
        ShipmentStatusEnum previousStatus = this.status;
        this.status = ShipmentStatusEnum.FAILED_DELIVERY;
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.DELIVERY_ATTEMPTED,
                location,
                "Delivery failed: " + reason,
                LocalDateTime.now()));

        registerEvent(ShipmentStatusChangedEvent.of(this.id, previousStatus, this.status, reason));
    }

    public void initiateReturn(String reason) {
        validateTransition(ShipmentStatusEnum.RETURNED);

        this.status = ShipmentStatusEnum.RETURNED;
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.RETURNED_TO_SENDER,
                null,
                "Return initiated: " + reason,
                LocalDateTime.now()));

        registerEvent(ShipmentReturnedEvent.of(this.id, reason));
    }

    public void cancel(String reason, String cancelledBy) {
        if (!status.isCancellable()) {
            throw new InvalidShipmentStateTransitionException(
                    "Cannot cancel shipment in status: " + status.name());
        }

        this.status = ShipmentStatusEnum.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        addTrackingEvent(TrackingEvent.create(
                TrackingEventStatusEnum.EXCEPTION,
                null,
                "Shipment cancelled: " + reason,
                LocalDateTime.now()));

        registerEvent(ShipmentCancelledEvent.of(this.id, reason, this.cancelledAt));
    }

    // ========== Private Helpers ==========

    private void validateTransition(ShipmentStatusEnum targetStatus) {
        if (!status.canTransitionTo(targetStatus)) {
            throw new InvalidShipmentStateTransitionException(
                    String.format("Cannot transition from %s to %s", status.name(), targetStatus.name()));
        }
    }

    public void setEstimatedDeliveryDate(LocalDateTime date) {
        this.estimatedDeliveryDate = date;
        this.updatedAt = LocalDateTime.now();
    }

    public void setWeight(Double weightKg) {
        this.weightKg = weightKg;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
        this.updatedAt = LocalDateTime.now();
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Domain Events ==========

    private void registerEvent(DomainEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents != null ? domainEvents : List.of());
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    // ========== Getters ==========

    public UUID getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public UUID getExternalOrderId() {
        return externalOrderId;
    }

    public UUID getExternalStoreId() {
        return externalStoreId;
    }

    public UUID getExternalCustomerId() {
        return externalCustomerId;
    }

    public UUID getCarrierId() {
        return carrierId;
    }

    public UUID getShippingMethodId() {
        return shippingMethodId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public String getShippingMethodName() {
        return shippingMethodName;
    }

    public Address getOriginAddress() {
        return originAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public ShipmentStatusEnum getStatus() {
        return status;
    }

    public Money getShippingCost() {
        return shippingCost;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ShipmentItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<TrackingEvent> getTrackingEvents() {
        return Collections.unmodifiableList(trackingEvents);
    }

    // ========== Builder for Reconstitution ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Shipment shipment = new Shipment();

        public Builder id(UUID id) {
            shipment.id = id;
            return this;
        }

        public Builder trackingNumber(String num) {
            shipment.trackingNumber = num;
            return this;
        }

        public Builder externalOrderId(UUID id) {
            shipment.externalOrderId = id;
            return this;
        }

        public Builder externalStoreId(UUID id) {
            shipment.externalStoreId = id;
            return this;
        }

        public Builder externalCustomerId(UUID id) {
            shipment.externalCustomerId = id;
            return this;
        }

        public Builder carrierId(UUID id) {
            shipment.carrierId = id;
            return this;
        }

        public Builder shippingMethodId(UUID id) {
            shipment.shippingMethodId = id;
            return this;
        }

        public Builder carrierCode(String code) {
            shipment.carrierCode = code;
            return this;
        }

        public Builder carrierName(String name) {
            shipment.carrierName = name;
            return this;
        }

        public Builder shippingMethodName(String name) {
            shipment.shippingMethodName = name;
            return this;
        }

        public Builder originAddress(Address addr) {
            shipment.originAddress = addr;
            return this;
        }

        public Builder destinationAddress(Address addr) {
            shipment.destinationAddress = addr;
            return this;
        }

        public Builder status(ShipmentStatusEnum status) {
            shipment.status = status;
            return this;
        }

        public Builder shippingCost(Money cost) {
            shipment.shippingCost = cost;
            return this;
        }

        public Builder weightKg(Double weight) {
            shipment.weightKg = weight;
            return this;
        }

        public Builder dimensions(String dim) {
            shipment.dimensions = dim;
            return this;
        }

        public Builder notes(String notes) {
            shipment.notes = notes;
            return this;
        }

        public Builder estimatedDeliveryDate(LocalDateTime dt) {
            shipment.estimatedDeliveryDate = dt;
            return this;
        }

        public Builder pickedUpAt(LocalDateTime dt) {
            shipment.pickedUpAt = dt;
            return this;
        }

        public Builder deliveredAt(LocalDateTime dt) {
            shipment.deliveredAt = dt;
            return this;
        }

        public Builder cancelledAt(LocalDateTime dt) {
            shipment.cancelledAt = dt;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            shipment.createdAt = dt;
            return this;
        }

        public Builder updatedAt(LocalDateTime dt) {
            shipment.updatedAt = dt;
            return this;
        }

        public Builder items(List<ShipmentItem> items) {
            shipment.items = new ArrayList<>(items);
            return this;
        }

        public Builder trackingEvents(List<TrackingEvent> events) {
            shipment.trackingEvents = new ArrayList<>(events);
            return this;
        }

        public Shipment build() {
            return shipment;
        }
    }
}
