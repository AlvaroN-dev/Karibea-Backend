package com.microservice.search.domain.port.in;

import com.microservice.search.domain.models.SearchQuery;
import com.microservice.search.domain.models.SearchResult;

/**
 * Puerto de entrada para ejecutar búsquedas de productos.
 */
public interface SearchProductsUseCase {

    /**
     * Ejecuta una búsqueda de productos basada en la query proporcionada.
     *
     * @param query parámetros de búsqueda
     * @return resultado con productos encontrados y metadata de paginación
     */
    SearchResult execute(SearchQuery query);
}
