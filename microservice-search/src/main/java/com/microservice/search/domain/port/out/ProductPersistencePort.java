package com.microservice.search.domain.port.out;

import com.microservice.search.domain.models.ProductId;
import com.microservice.search.domain.models.SearchableProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para persistencia de productos en PostgreSQL.
 */
public interface ProductPersistencePort {

    /**
     * Guarda un producto en la base de datos.
     *
     * @param product producto a guardar
     * @return producto guardado
     */
    SearchableProduct save(SearchableProduct product);

    /**
     * Guarda múltiples productos en batch.
     *
     * @param products lista de productos
     * @return productos guardados
     */
    List<SearchableProduct> saveAll(List<SearchableProduct> products);

    /**
     * Busca un producto por su ID externo.
     *
     * @param productId ID del producto
     * @return producto si existe
     */
    Optional<SearchableProduct> findByExternalProductId(UUID productId);

    /**
     * Busca productos por tienda.
     *
     * @param storeId ID de la tienda
     * @return lista de productos
     */
    List<SearchableProduct> findByStoreId(UUID storeId);

    /**
     * Elimina un producto (soft delete).
     *
     * @param productId ID del producto
     */
    void delete(ProductId productId);

    /**
     * Obtiene todos los productos activos.
     *
     * @return lista de productos activos
     */
    List<SearchableProduct> findAllActive();

    /**
     * Verifica si existe un producto.
     *
     * @param productId ID del producto
     * @return true si existe
     */
    boolean existsByExternalProductId(UUID productId);
}
