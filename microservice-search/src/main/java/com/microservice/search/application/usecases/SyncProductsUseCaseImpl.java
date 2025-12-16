package com.microservice.search.application.usecases;

import com.microservice.search.application.mapper.ProductMapper;
import com.microservice.search.domain.models.SearchableProduct;
import com.microservice.search.domain.port.in.IndexProductUseCase;
import com.microservice.search.domain.port.in.SyncProductsUseCase;
import com.microservice.search.domain.port.out.CatalogClientPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementación del caso de uso de sincronización de productos desde catálogo.
 */
@Service
public class SyncProductsUseCaseImpl implements SyncProductsUseCase {

    private static final Logger log = LoggerFactory.getLogger(SyncProductsUseCaseImpl.class);

    private final CatalogClientPort catalogClientPort;
    private final IndexProductUseCase indexProductUseCase;
    private final ProductMapper productMapper;

    public SyncProductsUseCaseImpl(
            CatalogClientPort catalogClientPort,
            IndexProductUseCase indexProductUseCase,
            ProductMapper productMapper) {
        this.catalogClientPort = catalogClientPort;
        this.indexProductUseCase = indexProductUseCase;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<Integer> syncAllProducts() {
        log.info("Starting full sync of all products from catalog");

        AtomicInteger count = new AtomicInteger(0);

        return catalogClientPort.getAllActiveProducts()
                .doOnNext(dto -> {
                    try {
                        SearchableProduct product = productMapper.toDomain(dto);
                        indexProductUseCase.execute(product);
                        count.incrementAndGet();
                    } catch (Exception e) {
                        log.error("Error syncing product {}: {}", dto.id(), e.getMessage());
                    }
                })
                .then(Mono.fromSupplier(count::get))
                .doOnSuccess(total -> log.info("Full sync completed. {} products synced", total));
    }

    @Override
    public Mono<Integer> syncProductsByStore(UUID storeId) {
        log.info("Starting sync of products for store: {}", storeId);

        AtomicInteger count = new AtomicInteger(0);

        return catalogClientPort.getProductsByStore(storeId)
                .doOnNext(dto -> {
                    try {
                        SearchableProduct product = productMapper.toDomain(dto);
                        indexProductUseCase.execute(product);
                        count.incrementAndGet();
                    } catch (Exception e) {
                        log.error("Error syncing product {}: {}", dto.id(), e.getMessage());
                    }
                })
                .then(Mono.fromSupplier(count::get))
                .doOnSuccess(total -> log.info("Store sync completed. {} products synced for store {}",
                        total, storeId));
    }

    @Override
    public Mono<Boolean> syncProduct(UUID productId) {
        log.info("Syncing single product: {}", productId);

        return catalogClientPort.getProduct(productId)
                .map(dto -> {
                    SearchableProduct product = productMapper.toDomain(dto);
                    indexProductUseCase.execute(product);
                    return true;
                })
                .doOnSuccess(success -> log.info("Product {} synced successfully", productId))
                .onErrorResume(e -> {
                    log.error("Error syncing product {}: {}", productId, e.getMessage());
                    return Mono.just(false);
                });
    }
}
