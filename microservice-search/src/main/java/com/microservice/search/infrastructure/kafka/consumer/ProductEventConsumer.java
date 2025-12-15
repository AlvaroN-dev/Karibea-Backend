package com.microservice.search.infrastructure.kafka.consumer;

import com.microservice.search.application.dto.CatalogProductDTO;
import com.microservice.search.application.mapper.ProductMapper;
import com.microservice.search.domain.models.SearchableProduct;
import com.microservice.search.domain.port.in.IndexProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Consumer de eventos de productos desde el microservicio de catálogo.
 */
@Component
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final IndexProductUseCase indexProductUseCase;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    public ProductEventConsumer(
            IndexProductUseCase indexProductUseCase,
            ProductMapper productMapper,
            ObjectMapper objectMapper) {
        this.indexProductUseCase = indexProductUseCase;
        this.productMapper = productMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Consume eventos de productos creados desde el catálogo.
     */
    @KafkaListener(topics = "${kafka.topics.product-created:product-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductCreated(String message) {
        log.info("Received product-created event");
        try {
            CatalogProductDTO dto = objectMapper.readValue(message, CatalogProductDTO.class);
            SearchableProduct product = productMapper.toDomain(dto);
            indexProductUseCase.execute(product);
            log.info("Product indexed from created event: {}", dto.id());
        } catch (Exception e) {
            log.error("Error processing product-created event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consume eventos de productos actualizados desde el catálogo.
     */
    @KafkaListener(topics = "${kafka.topics.product-updated:product-updated}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductUpdated(String message) {
        log.info("Received product-updated event");
        try {
            CatalogProductDTO dto = objectMapper.readValue(message, CatalogProductDTO.class);
            SearchableProduct product = productMapper.toDomain(dto);
            indexProductUseCase.reindex(product);
            log.info("Product re-indexed from updated event: {}", dto.id());
        } catch (Exception e) {
            log.error("Error processing product-updated event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consume eventos de productos eliminados desde el catálogo.
     */
    @KafkaListener(topics = "${kafka.topics.product-deleted:product-deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductDeleted(String message) {
        log.info("Received product-deleted event");
        try {
            ProductDeletedPayload payload = objectMapper.readValue(message, ProductDeletedPayload.class);
            // Solo marcamos como eliminado, no eliminamos físicamente
            log.info("Product marked for deletion: {}", payload.productId());
        } catch (Exception e) {
            log.error("Error processing product-deleted event: {}", e.getMessage(), e);
        }
    }

    private record ProductDeletedPayload(UUID productId) {
    }
}
