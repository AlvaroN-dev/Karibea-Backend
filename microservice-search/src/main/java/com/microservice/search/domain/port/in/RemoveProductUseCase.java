package com.microservice.search.domain.port.in;

import com.microservice.search.domain.models.ProductId;

/**
 * Puerto de entrada para eliminar productos del índice.
 */
public interface RemoveProductUseCase {

    /**
     * Elimina un producto del índice de búsqueda.
     *
     * @param productId ID del producto a eliminar
     */
    void execute(ProductId productId);
}
