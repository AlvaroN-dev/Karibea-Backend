package com.microservice.shipping.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new shipment.
 * Contains all necessary information to create a shipment record.
 */
@Schema(description = "Request to create a new shipment")
public class CreateShipmentRequest {

    @Schema(
            description = "Unique identifier of the associated order",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @Schema(
            description = "Unique identifier of the store fulfilling the order",
            example = "550e8400-e29b-41d4-a716-446655440002"
    )
    private UUID storeId;

    @Schema(
            description = "Unique identifier of the customer receiving the shipment",
            example = "550e8400-e29b-41d4-a716-446655440003"
    )
    private UUID customerId;

    @Schema(
            description = "Unique identifier of the carrier handling the shipment",
            example = "550e8400-e29b-41d4-a716-446655440004",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Carrier ID is required")
    private UUID carrierId;

    @Schema(
            description = "Unique identifier of the shipping method selected",
            example = "550e8400-e29b-41d4-a716-446655440005"
    )
    private UUID shippingMethodId;

    @Schema(
            description = "Code identifier for the carrier (e.g., FEDEX, UPS)",
            example = "FEDEX",
            maxLength = 50
    )
    @Size(max = 50, message = "Carrier code must not exceed 50 characters")
    private String carrierCode;

    @Schema(
            description = "Display name of the carrier",
            example = "FedEx Express",
            maxLength = 100
    )
    @Size(max = 100, message = "Carrier name must not exceed 100 characters")
    private String carrierName;

    @Schema(
            description = "Name of the shipping method selected",
            example = "Express Overnight",
            maxLength = 255
    )
    @Size(max = 255, message = "Shipping method name must not exceed 255 characters")
    private String shippingMethodName;

    @Schema(description = "Origin address where the shipment will be picked up")
    @Valid
    private AddressRequest originAddress;

    @Schema(
            description = "Destination address for delivery",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Valid
    @NotNull(message = "Destination address is required")
    private AddressRequest destinationAddress;

    @Schema(
            description = "Total shipping cost",
            example = "15.99",
            minimum = "0"
    )
    @DecimalMin(value = "0.00", message = "Shipping cost cannot be negative")
    private BigDecimal shippingCost;

    @Schema(
            description = "List of items included in the shipment",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Valid
    @NotEmpty(message = "At least one item is required")
    private List<ShipmentItemRequest> items;

    @Schema(
            description = "Additional notes or special instructions for the shipment",
            example = "Handle with care - fragile items",
            maxLength = 1000
    )
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    // Constructors
    public CreateShipmentRequest() {
    }

    public CreateShipmentRequest(UUID orderId, UUID storeId, UUID customerId, UUID carrierId, 
                                  UUID shippingMethodId, String carrierCode, String carrierName,
                                  String shippingMethodName, AddressRequest originAddress,
                                  AddressRequest destinationAddress, BigDecimal shippingCost,
                                  List<ShipmentItemRequest> items, String notes) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.customerId = customerId;
        this.carrierId = carrierId;
        this.shippingMethodId = shippingMethodId;
        this.carrierCode = carrierCode;
        this.carrierName = carrierName;
        this.shippingMethodName = shippingMethodName;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.shippingCost = shippingCost;
        this.items = items;
        this.notes = notes;
    }

    // Getters and Setters
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getStoreId() { return storeId; }
    public void setStoreId(UUID storeId) { this.storeId = storeId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getCarrierId() { return carrierId; }
    public void setCarrierId(UUID carrierId) { this.carrierId = carrierId; }

    public UUID getShippingMethodId() { return shippingMethodId; }
    public void setShippingMethodId(UUID shippingMethodId) { this.shippingMethodId = shippingMethodId; }

    public String getCarrierCode() { return carrierCode; }
    public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }

    public String getShippingMethodName() { return shippingMethodName; }
    public void setShippingMethodName(String shippingMethodName) { this.shippingMethodName = shippingMethodName; }

    public AddressRequest getOriginAddress() { return originAddress; }
    public void setOriginAddress(AddressRequest originAddress) { this.originAddress = originAddress; }

    public AddressRequest getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(AddressRequest destinationAddress) { this.destinationAddress = destinationAddress; }

    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }

    public List<ShipmentItemRequest> getItems() { return items; }
    public void setItems(List<ShipmentItemRequest> items) { this.items = items; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Address information for shipment origin or destination.
     */
    @Schema(description = "Address information for shipment origin or destination")
    public static class AddressRequest {

        @Schema(
                description = "Street address including number and name",
                example = "123 Main Street, Suite 400",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 255
        )
        @NotBlank(message = "Street is required")
        @Size(max = 255, message = "Street must not exceed 255 characters")
        private String street;

        @Schema(
                description = "City name",
                example = "New York",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        private String city;

        @Schema(
                description = "State or province code/name",
                example = "NY",
                maxLength = 100
        )
        @Size(max = 100, message = "State must not exceed 100 characters")
        private String state;

        @Schema(
                description = "Postal or ZIP code",
                example = "10001",
                maxLength = 20
        )
        @Size(max = 20, message = "Zip code must not exceed 20 characters")
        private String zipCode;

        @Schema(
                description = "Country code or name",
                example = "USA",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        private String country;

        // Constructors
        public AddressRequest() {
        }

        public AddressRequest(String street, String city, String state, String zipCode, String country) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.country = country;
        }

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
    }

    /**
     * Item information for shipment.
     */
    @Schema(description = "Item included in the shipment")
    public static class ShipmentItemRequest {

        @Schema(
                description = "Reference to the original order item",
                example = "550e8400-e29b-41d4-a716-446655440010"
        )
        private UUID orderItemId;

        @Schema(
                description = "Reference to the product in the catalog",
                example = "550e8400-e29b-41d4-a716-446655440011"
        )
        private UUID productId;

        @Schema(
                description = "Name of the product",
                example = "Wireless Bluetooth Headphones",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 255
        )
        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name must not exceed 255 characters")
        private String productName;

        @Schema(
                description = "Stock keeping unit code",
                example = "WBH-001-BLK",
                maxLength = 100
        )
        @Size(max = 100, message = "SKU must not exceed 100 characters")
        private String sku;

        @Schema(
                description = "Quantity of items to ship",
                example = "2",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;

        // Constructors
        public ShipmentItemRequest() {
        }

        public ShipmentItemRequest(UUID orderItemId, UUID productId, String productName, String sku, int quantity) {
            this.orderItemId = orderItemId;
            this.productId = productId;
            this.productName = productName;
            this.sku = sku;
            this.quantity = quantity;
        }

        // Getters and Setters
        public UUID getOrderItemId() { return orderItemId; }
        public void setOrderItemId(UUID orderItemId) { this.orderItemId = orderItemId; }

        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
