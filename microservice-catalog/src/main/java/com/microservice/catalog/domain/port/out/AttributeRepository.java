package com.microservice.catalog.domain.port.out;

import com.microservice.catalog.domain.models.Attribute;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Attribute persistence operations.
 */
public interface AttributeRepository {

    /**
     * Saves an attribute (create or update).
     *
     * @param attribute the attribute to save
     * @return the saved attribute
     */
    Attribute save(Attribute attribute);

    /**
     * Finds an attribute by its ID.
     *
     * @param id the attribute ID
     * @return an Optional containing the attribute if found
     */
    Optional<Attribute> findById(UUID id);

    /**
     * Finds an attribute by its name.
     *
     * @param name the attribute name
     * @return an Optional containing the attribute if found
     */
    Optional<Attribute> findByName(String name);

    /**
     * Finds all attributes.
     *
     * @return list of all attributes
     */
    List<Attribute> findAll();

    /**
     * Deletes an attribute by its ID.
     *
     * @param id the attribute ID
     */
    void deleteById(UUID id);

    /**
     * Checks if an attribute exists with the given ID.
     *
     * @param id the attribute ID
     * @return true if exists
     */
    boolean existsById(UUID id);
}
