package com.microservice.catalog.domain.port.out;

import com.microservice.catalog.domain.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Product persistence operations.
 * Defines the contract that infrastructure layer must implement.
 * 
 * This follows the Dependency Inversion Principle (DIP) - the domain
 * defines what it needs, and infrastructure provides the implementation.
 */
public interface ProductRepository {

    /**
     * Saves a product (create or update).
     *
     * @param product the product to save
     * @return the saved product with updated version
     */
    Product save(Product product);

    /**
     * Finds a product by its ID.
     *
     * @param id the product ID
     * @return an Optional containing the product if found
     */
    Optional<Product> findById(UUID id);

    /**
     * Finds a product by its SKU within a store.
     *
     * @param sku     the product SKU
     * @param storeId the store ID
     * @return an Optional containing the product if found
     */
    Optional<Product> findBySkuAndStoreId(String sku, UUID storeId);

    /**
     * Finds all products for a given store.
     *
     * @param storeId the store ID
     * @return list of products
     */
    List<Product> findByStoreId(UUID storeId);

    /**
     * Finds all products with pagination.
     *
     * @param page page number (0-indexed)
     * @param size page size
     * @return list of products
     */
    List<Product> findAll(int page, int size);

    /**
     * Counts all products.
     *
     * @return total count
     */
    long count();

    /**
     * Deletes a product by its ID.
     *
     * @param id the product ID
     */
    void deleteById(UUID id);

    /**
     * Checks if a product exists with the given ID.
     *
     * @param id the product ID
     * @return true if exists
     */
    boolean existsById(UUID id);

    /**
     * Checks if a product with the given SKU exists in the store.
     *
     * @param sku     the product SKU
     * @param storeId the store ID
     * @return true if exists
     */
    boolean existsBySkuAndStoreId(String sku, UUID storeId);
}
