package com.microservice.search.domain.port.out;

import com.microservice.search.application.dto.CatalogProductDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Puerto de salida para comunicación con el microservicio de catálogo via
 * WebClient.
 */
public interface CatalogClientPort {

    /**
     * Obtiene un producto del catálogo.
     *
     * @param productId ID del producto
     * @return producto del catálogo
     */
    Mono<CatalogProductDTO> getProduct(UUID productId);

    /**
     * Obtiene todos los productos activos del catálogo.
     *
     * @return stream de productos
     */
    Flux<CatalogProductDTO> getAllActiveProducts();

    /**
     * Obtiene productos de una tienda específica.
     *
     * @param storeId ID de la tienda
     * @return stream de productos
     */
    Flux<CatalogProductDTO> getProductsByStore(UUID storeId);

    /**
     * Verifica conectividad con el servicio de catálogo.
     *
     * @return true si está disponible
     */
    Mono<Boolean> healthCheck();
}
