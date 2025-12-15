package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.domain.models.Intent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for intent persistence.
 * Location: domain/port/out - External dependency contract.
 */
public interface IntentRepositoryPort {

    /**
     * Finds an intent by its ID.
     *
     * @param id the intent UUID
     * @return optional containing the intent if found
     */
    Optional<Intent> findById(UUID id);

    /**
     * Finds an intent by its name.
     *
     * @param name the intent name
     * @return optional containing the intent if found
     */
    Optional<Intent> findByName(String name);

    /**
     * Finds all active intents.
     *
     * @return list of active intents
     */
    List<Intent> findAllActive();

    /**
     * Finds all intents.
     *
     * @return list of all intents
     */
    List<Intent> findAll();

    /**
     * Saves an intent.
     *
     * @param intent the intent to save
     * @return the saved intent
     */
    Intent save(Intent intent);
}
