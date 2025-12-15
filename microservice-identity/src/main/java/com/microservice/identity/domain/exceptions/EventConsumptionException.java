package com.microservice.identity.domain.exceptions;

/**
 * Exception thrown when event consumption fails.
 * Indicates that an event could not be processed by a consumer.
 * 
 * This exception should be handled by:
 * - Logging the error
 * - Retrying the operation
 * - Sending the event to Dead Letter Queue (DLQ)
 */
public class EventConsumptionException extends RuntimeException {

    private final String eventId;
    private final String eventType;
    private final String topic;
    private final int retryCount;

    /**
     * Constructor with event details.
     * 
     * @param message    Error message
     * @param eventId    Event ID
     * @param eventType  Event type
     * @param topic      Kafka topic
     * @param retryCount Number of retries attempted
     */
    public EventConsumptionException(String message, String eventId, String eventType, String topic, int retryCount) {
        super(message);
        this.eventId = eventId;
        this.eventType = eventType;
        this.topic = topic;
        this.retryCount = retryCount;
    }

    /**
     * Constructor with event details and cause.
     * 
     * @param message    Error message
     * @param eventId    Event ID
     * @param eventType  Event type
     * @param topic      Kafka topic
     * @param retryCount Number of retries attempted
     * @param cause      Root cause
     */
    public EventConsumptionException(String message, String eventId, String eventType, String topic, int retryCount,
            Throwable cause) {
        super(message, cause);
        this.eventId = eventId;
        this.eventType = eventType;
        this.topic = topic;
        this.retryCount = retryCount;
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

    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public String toString() {
        return "EventConsumptionException{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", topic='" + topic + '\'' +
                ", retryCount=" + retryCount +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
