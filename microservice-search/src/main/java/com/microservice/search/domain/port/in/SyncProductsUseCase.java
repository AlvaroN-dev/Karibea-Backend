package com.microservice.search.domain.port.in;

import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Puerto de entrada para sincronizar productos desde el microservicio de
 * catálogo.
 */
public interface SyncProductsUseCase {

    /**
     * Sincroniza todos los productos activos desde el catálogo.
     *
     * @return número de productos sincronizados
     */
    Mono<Integer> syncAllProducts();

    /**
     * Sincroniza productos de una tienda específica.
     *
     * @param storeId ID de la tienda
     * @return número de productos sincronizados
     */
    Mono<Integer> syncProductsByStore(UUID storeId);

    /**
     * Sincroniza un producto específico.
     *
     * @param productId ID del producto
     * @return true si se sincronizó correctamente
     */
    Mono<Boolean> syncProduct(UUID productId);
}
