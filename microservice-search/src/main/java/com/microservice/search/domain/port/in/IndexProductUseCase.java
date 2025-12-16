package com.microservice.search.domain.port.in;

import com.microservice.search.domain.models.SearchableProduct;

/**
 * Puerto de entrada para indexar productos en el motor de búsqueda.
 */
public interface IndexProductUseCase {

    /**
     * Indexa un producto para hacerlo buscable.
     *
     * @param product producto a indexar
     */
    void execute(SearchableProduct product);

    /**
     * Re-indexa un producto existente con datos actualizados.
     *
     * @param product producto con datos actualizados
     */
    void reindex(SearchableProduct product);
}
