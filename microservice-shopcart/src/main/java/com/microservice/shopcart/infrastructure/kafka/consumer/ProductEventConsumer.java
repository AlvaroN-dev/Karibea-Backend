package com.microservice.shopcart.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka consumer for handling product-related events from the Catalog microservice.
 * Reacts to product updates, deletions, and price changes to keep cart data consistent.
 */
@Component
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final ObjectMapper objectMapper;

    public ProductEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles product price updated events.
     * When a product price changes, this can trigger cart recalculations or notifications.
     */
    @KafkaListener(
        topics = "${kafka.topics.product-price-updated:catalog.product-price-updated}",
        groupId = "${spring.kafka.consumer.group-id:shopcart-service}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleProductPriceUpdated(String message) {
        try {
            log.info("Received product price updated event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            UUID productId = UUID.fromString(eventData.path("payload").path("productId").asText());
            
            // Log the event - actual implementation could update cart prices
            log.info("Product {} price was updated. Carts with this product may need recalculation.", productId);
            
            // Note: In production, you might want to:
            // 1. Find all carts containing this product
            // 2. Update the stored price or mark items for refresh
            // 3. Notify users about price changes
            
        } catch (Exception e) {
            log.error("Error processing product price updated event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles product deleted events.
     * When a product is deleted, items in carts should be flagged or removed.
     */
    @KafkaListener(
        topics = "${kafka.topics.product-deleted:catalog.product-deleted}",
        groupId = "${spring.kafka.consumer.group-id:shopcart-service}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleProductDeleted(String message) {
        try {
            log.info("Received product deleted event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            UUID productId = UUID.fromString(eventData.path("payload").path("productId").asText());
            
            log.warn("Product {} was deleted. Items in active carts should be flagged.", productId);
            
            // Note: In production, you might want to:
            // 1. Find all carts containing this product
            // 2. Mark items as unavailable
            // 3. Notify users that an item in their cart is no longer available
            
        } catch (Exception e) {
            log.error("Error processing product deleted event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles product stock updated events.
     * When stock changes, carts may need to adjust quantities.
     */
    @KafkaListener(
        topics = "${kafka.topics.product-stock-updated:inventory.stock-updated}",
        groupId = "${spring.kafka.consumer.group-id:shopcart-service}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleStockUpdated(String message) {
        try {
            log.info("Received stock updated event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            UUID productId = UUID.fromString(eventData.path("payload").path("productId").asText());
            int newStock = eventData.path("payload").path("availableQuantity").asInt();
            
            log.info("Product {} stock updated to {}. Checking cart reservations.", productId, newStock);
            
            // Note: In production, you might want to:
            // 1. Validate cart item quantities against new stock
            // 2. Release or adjust inventory reservations
            // 3. Notify users if their desired quantity is no longer available
            
        } catch (Exception e) {
            log.error("Error processing stock updated event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles coupon invalidated events from Marketing service.
     * When a coupon is invalidated, it should be removed from active carts.
     */
    @KafkaListener(
        topics = "${kafka.topics.coupon-invalidated:marketing.coupon-invalidated}",
        groupId = "${spring.kafka.consumer.group-id:shopcart-service}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCouponInvalidated(String message) {
        try {
            log.info("Received coupon invalidated event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            String couponCode = eventData.path("payload").path("couponCode").asText();
            
            log.warn("Coupon '{}' was invalidated. Should be removed from active carts.", couponCode);
            
            // Note: In production, you might want to:
            // 1. Find all carts with this coupon applied
            // 2. Remove the coupon and recalculate totals
            // 3. Notify users that a coupon was removed
            
        } catch (Exception e) {
            log.error("Error processing coupon invalidated event: {}", e.getMessage(), e);
        }
    }
}
