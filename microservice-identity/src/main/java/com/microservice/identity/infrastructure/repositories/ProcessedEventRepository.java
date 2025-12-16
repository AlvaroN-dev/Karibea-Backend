package com.microservice.identity.infrastructure.repositories;

import com.microservice.identity.infrastructure.entities.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository for ProcessedEventEntity.
 * Provides methods to check if events have been processed and track processed
 * events.
 */
@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, String> {

    /**
     * Check if an event has already been processed.
     * 
     * @param eventId Event ID
     * @return true if event exists in database
     */
    boolean existsByEventId(String eventId);

    /**
     * Find all events processed before a certain timestamp.
     * Useful for cleanup of old processed events.
     * 
     * @param timestamp Timestamp threshold
     * @return List of processed events
     */
    List<ProcessedEventEntity> findByProcessedAtBefore(Instant timestamp);

    /**
     * Delete all events processed before a certain timestamp.
     * Useful for cleanup of old processed events.
     * 
     * @param timestamp Timestamp threshold
     */
    void deleteByProcessedAtBefore(Instant timestamp);
}
