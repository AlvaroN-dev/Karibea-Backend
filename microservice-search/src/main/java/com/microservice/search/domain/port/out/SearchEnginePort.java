package com.microservice.search.domain.port.out;

import com.microservice.search.domain.models.ProductId;
import com.microservice.search.domain.models.SearchQuery;
import com.microservice.search.domain.models.SearchResult;
import com.microservice.search.domain.models.SearchableProduct;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones con el motor de búsqueda (Meilisearch).
 */
public interface SearchEnginePort {

    /**
     * Ejecuta una búsqueda en el motor de búsqueda.
     *
     * @param query parámetros de búsqueda
     * @return resultado con productos encontrados
     */
    SearchResult search(SearchQuery query);

    /**
     * Indexa un producto en el motor de búsqueda.
     *
     * @param product producto a indexar
     */
    void index(SearchableProduct product);

    /**
     * Indexa múltiples productos en batch.
     *
     * @param products lista de productos a indexar
     */
    void indexBatch(List<SearchableProduct> products);

    /**
     * Elimina un producto del índice.
     *
     * @param productId ID del producto
     */
    void delete(ProductId productId);

    /**
     * Busca un producto por ID en el índice.
     *
     * @param productId ID del producto
     * @return producto si existe
     */
    Optional<SearchableProduct> findById(ProductId productId);

    /**
     * Verifica si un producto está indexado.
     *
     * @param productId ID del producto
     * @return true si existe
     */
    boolean exists(ProductId productId);
}
