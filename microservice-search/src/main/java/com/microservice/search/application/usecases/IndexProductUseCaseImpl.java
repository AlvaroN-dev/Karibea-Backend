package com.microservice.search.application.usecases;

import com.microservice.search.domain.events.ProductIndexedEvent;
import com.microservice.search.domain.models.SearchableProduct;
import com.microservice.search.domain.port.in.IndexProductUseCase;
import com.microservice.search.domain.port.out.EventPublisherPort;
import com.microservice.search.domain.port.out.ProductPersistencePort;
import com.microservice.search.domain.port.out.SearchEnginePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso de indexación de productos.
 */
@Service
public class IndexProductUseCaseImpl implements IndexProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(IndexProductUseCaseImpl.class);

    private final SearchEnginePort searchEnginePort;
    private final ProductPersistencePort persistencePort;
    private final EventPublisherPort eventPublisherPort;

    public IndexProductUseCaseImpl(
            SearchEnginePort searchEnginePort,
            ProductPersistencePort persistencePort,
            EventPublisherPort eventPublisherPort) {
        this.searchEnginePort = searchEnginePort;
        this.persistencePort = persistencePort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    @Transactional
    public void execute(SearchableProduct product) {
        log.info("Indexing product: {} - {}", product.id(), product.name());

        // 1. Guardar en PostgreSQL
        SearchableProduct saved = persistencePort.save(product);
        log.debug("Product saved to PostgreSQL: {}", saved.id());

        // 2. Indexar en Meilisearch
        searchEnginePort.index(saved);
        log.debug("Product indexed in Meilisearch: {}", saved.id());

        // 3. Publicar evento
        eventPublisherPort.publish(ProductIndexedEvent.of(product.id().value()));
        log.info("Product indexed successfully: {}", product.id());
    }

    @Override
    @Transactional
    public void reindex(SearchableProduct product) {
        log.info("Re-indexing product: {}", product.id());

        SearchableProduct updated = product.withUpdatedTimestamp();

        // Actualizar en PostgreSQL
        persistencePort.save(updated);

        // Re-indexar en Meilisearch
        searchEnginePort.index(updated);

        log.info("Product re-indexed successfully: {}", product.id());
    }
}
