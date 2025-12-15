package com.microservice.identity.domain.exceptions;

/**
 * Exception thrown when event publishing fails.
 * Indicates that an event could not be published to Kafka.
 * 
 * This exception should be handled by:
 * - Logging the error
 * - Retrying the operation
 * - Storing the event for later retry (outbox pattern)
 */
public class EventPublishingException extends RuntimeException {

    private final String eventId;
    private final String eventType;
    private final String topic;

    /**
     * Constructor with event details.
     * 
     * @param message   Error message
     * @param eventId   Event ID
     * @param eventType Event type
     * @param topic     Kafka topic
     */
    public EventPublishingException(String message, String eventId, String eventType, String topic) {
        super(message);
        this.eventId = eventId;
        this.eventType = eventType;
        this.topic = topic;
    }

    /**
     * Constructor with event details and cause.
     * 
     * @param message   Error message
     * @param eventId   Event ID
     * @param eventType Event type
     * @param topic     Kafka topic
     * @param cause     Root cause
     */
    public EventPublishingException(String message, String eventId, String eventType, String topic, Throwable cause) {
        super(message, cause);
        this.eventId = eventId;
        this.eventType = eventType;
        this.topic = topic;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "EventPublishingException{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", topic='" + topic + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
