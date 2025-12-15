package com.microservice.identity.infrastructure.entities;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity for tracking processed events to ensure idempotency.
 * Stores event IDs that have been successfully processed to prevent duplicate
 * processing.
 * 
 * This is critical for at-least-once delivery semantics where:
 * - Kafka may deliver the same message multiple times
 * - Consumer crashes after processing but before committing offset
 * - Network issues cause retries
 */
@Entity
@Table(name = "processed_events", indexes = {
        @Index(name = "idx_event_id", columnList = "event_id", unique = true),
        @Index(name = "idx_processed_at", columnList = "processed_at")
})
public class ProcessedEventEntity {

    @Id
    @Column(name = "event_id", nullable = false, length = 100)
    private String eventId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "topic", nullable = false, length = 100)
    private String topic;

    @Column(name = "partition_id", nullable = false)
    private Integer partitionId;

    @Column(name = "offset_value", nullable = false)
    private Long offsetValue;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    /**
     * No-args constructor for JPA.
     */
    public ProcessedEventEntity() {
    }

    /**
     * Constructor with all fields.
     */
    public ProcessedEventEntity(String eventId, String eventType, String aggregateId,
            String topic, Integer partitionId, Long offsetValue) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.topic = topic;
        this.partitionId = partitionId;
        this.offsetValue = offsetValue;
        this.processedAt = Instant.now();
    }

    // Getters and setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    public Long getOffsetValue() {
        return offsetValue;
    }

    public void setOffsetValue(Long offsetValue) {
        this.offsetValue = offsetValue;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }
}
