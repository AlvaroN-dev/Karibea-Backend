package com.microservice.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new order.
 * Contains customer, store, shipping/billing addresses, and order items.
 */
@Schema(description = "Request to create a new order")
public class CreateOrderRequest {

    @Schema(description = "Unique identifier of the customer placing the order", example = "550e8400-e29b-41d4-a716-446655440100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @Schema(description = "Unique identifier of the store", example = "660e8400-e29b-41d4-a716-446655440200", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Store ID is required")
    private UUID storeId;

    @Schema(description = "Currency code for the order", example = "USD")
    private String currency;

    @Schema(description = "Shipping address for the order", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull(message = "Shipping address is required")
    private AddressRequest shippingAddress;

    @Schema(description = "Billing address for the order (optional, defaults to shipping address)")
    @Valid
    private AddressRequest billingAddress;

    @Schema(description = "List of items to be ordered", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotEmpty(message = "At least one item is required")
    private List<OrderItemRequest> items;

    @Schema(description = "Optional notes from the customer", example = "Please deliver between 9 AM and 5 PM")
    private String customerNotes;

    // Constructors
    public CreateOrderRequest() {
    }

    public CreateOrderRequest(UUID customerId, UUID storeId, String currency,
                              AddressRequest shippingAddress, AddressRequest billingAddress,
                              List<OrderItemRequest> items, String customerNotes) {
        this.customerId = customerId;
        this.storeId = storeId;
        this.currency = currency;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.items = items;
        this.customerNotes = customerNotes;
    }

    // Getters and Setters
    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AddressRequest getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressRequest shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressRequest getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressRequest billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    /**
     * Address information for shipping or billing.
     */
    @Schema(description = "Address information")
    public static class AddressRequest {

        @Schema(description = "Street address", example = "123 Main Street", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Street is required")
        private String street;

        @Schema(description = "City", example = "New York", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "City is required")
        private String city;

        @Schema(description = "State or province", example = "NY")
        private String state;

        @Schema(description = "ZIP or postal code", example = "10001")
        private String zipCode;

        @Schema(description = "Country", example = "USA", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Country is required")
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
        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    /**
     * Order item information.
     */
    @Schema(description = "Order item details")
    public static class OrderItemRequest {

        @Schema(description = "Unique identifier of the product", example = "770e8400-e29b-41d4-a716-446655440300", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Product ID is required")
        private UUID productId;

        @Schema(description = "Name of the product", example = "Wireless Headphones", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Product name is required")
        private String productName;

        @Schema(description = "Product variant name", example = "Black")
        private String variantName;

        @Schema(description = "Stock Keeping Unit", example = "WH-001-BLK")
        private String sku;

        @Schema(description = "URL of the product image", example = "https://example.com/images/headphones.jpg")
        private String imageUrl;

        @Schema(description = "Price per unit", example = "99.99", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Unit price is required")
        private BigDecimal unitPrice;

        @Schema(description = "Quantity of items", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;

        // Constructors
        public OrderItemRequest() {
        }

        public OrderItemRequest(UUID productId, String productName, String variantName,
                                String sku, String imageUrl, BigDecimal unitPrice, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.variantName = variantName;
            this.sku = sku;
            this.imageUrl = imageUrl;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        // Getters and Setters
        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getVariantName() {
            return variantName;
        }

        public void setVariantName(String variantName) {
            this.variantName = variantName;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
