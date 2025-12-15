package com.microservice.shipping.application.dto.response;

import com.microservice.shipping.domain.models.ShipmentStatusEnum;
import com.microservice.shipping.domain.models.TrackingEventStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Shipment with enriched external entity information.
 * Contains complete shipment data including order, store, customer, and carrier details.
 */
@Schema(description = "Complete shipment information with enriched external entity data")
public class ShipmentResponse {

    @Schema(description = "Unique identifier of the shipment", example = "550e8400-e29b-41d4-a716-446655440100")
    private UUID id;

    @Schema(description = "Unique tracking number for carrier lookup", example = "FEDEX-20241215-A1B2C3D4")
    private String trackingNumber;

    @Schema(description = "Order information associated with this shipment")
    private OrderInfoResponse order;

    @Schema(description = "Store fulfilling the shipment")
    private StoreInfoResponse store;

    @Schema(description = "Customer receiving the shipment")
    private CustomerInfoResponse customer;

    @Schema(description = "Carrier handling the shipment")
    private CarrierInfoResponse carrier;

    @Schema(description = "Shipping method name", example = "Express Overnight")
    private String shippingMethodName;

    @Schema(description = "Current status of the shipment", example = "IN_TRANSIT")
    private ShipmentStatusEnum status;

    @Schema(description = "Origin address (pickup location)")
    private AddressResponse originAddress;

    @Schema(description = "Destination address (delivery location)")
    private AddressResponse destinationAddress;

    @Schema(description = "Shipping cost", example = "15.99")
    private BigDecimal shippingCost;

    @Schema(description = "Total weight in kilograms", example = "2.5")
    private Double weightKg;

    @Schema(description = "Package dimensions (LxWxH)", example = "30x20x15 cm")
    private String dimensions;

    @Schema(description = "Additional notes or instructions", example = "Handle with care - fragile")
    private String notes;

    @Schema(description = "Estimated delivery date and time", example = "2024-12-18T17:00:00")
    private LocalDateTime estimatedDeliveryDate;

    @Schema(description = "Timestamp when the package was picked up", example = "2024-12-15T14:30:00")
    private LocalDateTime pickedUpAt;

    @Schema(description = "Timestamp when the package was delivered", example = "2024-12-18T16:45:00")
    private LocalDateTime deliveredAt;

    @Schema(description = "Timestamp when the shipment was cancelled")
    private LocalDateTime cancelledAt;

    @Schema(description = "Timestamp when the shipment was created", example = "2024-12-15T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the shipment was last updated", example = "2024-12-15T14:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "List of items included in the shipment")
    private List<ShipmentItemResponse> items;

    @Schema(description = "Tracking event history in chronological order")
    private List<TrackingEventResponse> trackingEvents;

    // Constructors
    public ShipmentResponse() {
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public OrderInfoResponse getOrder() { return order; }
    public void setOrder(OrderInfoResponse order) { this.order = order; }

    public StoreInfoResponse getStore() { return store; }
    public void setStore(StoreInfoResponse store) { this.store = store; }

    public CustomerInfoResponse getCustomer() { return customer; }
    public void setCustomer(CustomerInfoResponse customer) { this.customer = customer; }

    public CarrierInfoResponse getCarrier() { return carrier; }
    public void setCarrier(CarrierInfoResponse carrier) { this.carrier = carrier; }

    public String getShippingMethodName() { return shippingMethodName; }
    public void setShippingMethodName(String shippingMethodName) { this.shippingMethodName = shippingMethodName; }

    public ShipmentStatusEnum getStatus() { return status; }
    public void setStatus(ShipmentStatusEnum status) { this.status = status; }

    public AddressResponse getOriginAddress() { return originAddress; }
    public void setOriginAddress(AddressResponse originAddress) { this.originAddress = originAddress; }

    public AddressResponse getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(AddressResponse destinationAddress) { this.destinationAddress = destinationAddress; }

    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }

    public LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ShipmentItemResponse> getItems() { return items; }
    public void setItems(List<ShipmentItemResponse> items) { this.items = items; }

    public List<TrackingEventResponse> getTrackingEvents() { return trackingEvents; }
    public void setTrackingEvents(List<TrackingEventResponse> trackingEvents) { this.trackingEvents = trackingEvents; }

    // ==================== NESTED CLASSES ====================

    /**
     * Address information for origin or destination.
     */
    @Schema(description = "Address information")
    public static class AddressResponse {

        @Schema(description = "Street address", example = "123 Main Street, Suite 400")
        private String street;

        @Schema(description = "City name", example = "New York")
        private String city;

        @Schema(description = "State or province", example = "NY")
        private String state;

        @Schema(description = "Postal/ZIP code", example = "10001")
        private String zipCode;

        @Schema(description = "Country", example = "USA")
        private String country;

        @Schema(description = "Full formatted address", example = "123 Main Street, Suite 400, New York, NY 10001, USA")
        private String fullAddress;

        public AddressResponse() {}

        public AddressResponse(String street, String city, String state, String zipCode, 
                               String country, String fullAddress) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.country = country;
            this.fullAddress = fullAddress;
        }

        public static Builder builder() { return new Builder(); }

        // Getters and Setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getFullAddress() { return fullAddress; }
        public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

        public static class Builder {
            private final AddressResponse instance = new AddressResponse();
            public Builder street(String street) { instance.street = street; return this; }
            public Builder city(String city) { instance.city = city; return this; }
            public Builder state(String state) { instance.state = state; return this; }
            public Builder zipCode(String zipCode) { instance.zipCode = zipCode; return this; }
            public Builder country(String country) { instance.country = country; return this; }
            public Builder fullAddress(String fullAddress) { instance.fullAddress = fullAddress; return this; }
            public AddressResponse build() { return instance; }
        }
    }

    /**
     * Shipment item information with enriched product data.
     */
    @Schema(description = "Item included in the shipment")
    public static class ShipmentItemResponse {

        @Schema(description = "Unique identifier of the shipment item", example = "550e8400-e29b-41d4-a716-446655440200")
        private UUID id;

        @Schema(description = "Product information from catalog")
        private ProductInfoResponse product;

        @Schema(description = "Product name at time of shipment", example = "Wireless Bluetooth Headphones")
        private String productName;

        @Schema(description = "SKU at time of shipment", example = "WBH-001-BLK")
        private String sku;

        @Schema(description = "Quantity of this item", example = "2")
        private int quantity;

        public ShipmentItemResponse() {}

        public static Builder builder() { return new Builder(); }

        // Getters and Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public ProductInfoResponse getProduct() { return product; }
        public void setProduct(ProductInfoResponse product) { this.product = product; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public static class Builder {
            private final ShipmentItemResponse instance = new ShipmentItemResponse();
            public Builder id(UUID id) { instance.id = id; return this; }
            public Builder product(ProductInfoResponse product) { instance.product = product; return this; }
            public Builder productName(String productName) { instance.productName = productName; return this; }
            public Builder sku(String sku) { instance.sku = sku; return this; }
            public Builder quantity(int quantity) { instance.quantity = quantity; return this; }
            public ShipmentItemResponse build() { return instance; }
        }
    }

    /**
     * Tracking event information.
     */
    @Schema(description = "Tracking event in the shipment history")
    public static class TrackingEventResponse {

        @Schema(description = "Unique identifier of the tracking event", example = "550e8400-e29b-41d4-a716-446655440300")
        private UUID id;

        @Schema(description = "Status of the tracking event", example = "IN_TRANSIT")
        private TrackingEventStatusEnum status;

        @Schema(description = "Location where the event occurred", example = "Distribution Center, Miami, FL")
        private String location;

        @Schema(description = "Description of the event", example = "Package arrived at distribution center")
        private String description;

        @Schema(description = "When the event occurred", example = "2024-12-15T14:30:00")
        private LocalDateTime occurredAt;

        @Schema(description = "When the event was recorded", example = "2024-12-15T14:31:00")
        private LocalDateTime createdAt;

        public TrackingEventResponse() {}

        public static Builder builder() { return new Builder(); }

        // Getters and Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public TrackingEventStatusEnum getStatus() { return status; }
        public void setStatus(TrackingEventStatusEnum status) { this.status = status; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getOccurredAt() { return occurredAt; }
        public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public static class Builder {
            private final TrackingEventResponse instance = new TrackingEventResponse();
            public Builder id(UUID id) { instance.id = id; return this; }
            public Builder status(TrackingEventStatusEnum status) { instance.status = status; return this; }
            public Builder location(String location) { instance.location = location; return this; }
            public Builder description(String description) { instance.description = description; return this; }
            public Builder occurredAt(LocalDateTime occurredAt) { instance.occurredAt = occurredAt; return this; }
            public Builder createdAt(LocalDateTime createdAt) { instance.createdAt = createdAt; return this; }
            public TrackingEventResponse build() { return instance; }
        }
    }

    // ==================== BUILDER ====================

    public static class Builder {
        private final ShipmentResponse instance = new ShipmentResponse();

        public Builder id(UUID id) { instance.id = id; return this; }
        public Builder trackingNumber(String trackingNumber) { instance.trackingNumber = trackingNumber; return this; }
        public Builder order(OrderInfoResponse order) { instance.order = order; return this; }
        public Builder store(StoreInfoResponse store) { instance.store = store; return this; }
        public Builder customer(CustomerInfoResponse customer) { instance.customer = customer; return this; }
        public Builder carrier(CarrierInfoResponse carrier) { instance.carrier = carrier; return this; }
        public Builder shippingMethodName(String shippingMethodName) { instance.shippingMethodName = shippingMethodName; return this; }
        public Builder status(ShipmentStatusEnum status) { instance.status = status; return this; }
        public Builder originAddress(AddressResponse originAddress) { instance.originAddress = originAddress; return this; }
        public Builder destinationAddress(AddressResponse destinationAddress) { instance.destinationAddress = destinationAddress; return this; }
        public Builder shippingCost(BigDecimal shippingCost) { instance.shippingCost = shippingCost; return this; }
        public Builder weightKg(Double weightKg) { instance.weightKg = weightKg; return this; }
        public Builder dimensions(String dimensions) { instance.dimensions = dimensions; return this; }
        public Builder notes(String notes) { instance.notes = notes; return this; }
        public Builder estimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) { instance.estimatedDeliveryDate = estimatedDeliveryDate; return this; }
        public Builder pickedUpAt(LocalDateTime pickedUpAt) { instance.pickedUpAt = pickedUpAt; return this; }
        public Builder deliveredAt(LocalDateTime deliveredAt) { instance.deliveredAt = deliveredAt; return this; }
        public Builder cancelledAt(LocalDateTime cancelledAt) { instance.cancelledAt = cancelledAt; return this; }
        public Builder createdAt(LocalDateTime createdAt) { instance.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { instance.updatedAt = updatedAt; return this; }
        public Builder items(List<ShipmentItemResponse> items) { instance.items = items; return this; }
        public Builder trackingEvents(List<TrackingEventResponse> trackingEvents) { instance.trackingEvents = trackingEvents; return this; }

        public ShipmentResponse build() { return instance; }
    }
}
