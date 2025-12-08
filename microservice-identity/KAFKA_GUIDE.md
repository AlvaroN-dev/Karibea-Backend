# Kafka Configuration Guide

## Overview

This microservice uses Apache Kafka for event-driven architecture. Events are published when users are created, updated, or deleted, and when email verification is required.

## Architecture

### Hexagonal Architecture Compliance

- **Domain Layer**: Event models (`DomainEvent`, `UserCreatedEvent`, etc.) and `EventPublisherPort` interface
- **Infrastructure Layer**: Kafka configurations, `UserEventProducer` adapter, and event consumers
- **Application Layer**: Uses `EventPublisherPort` without knowing about Kafka

### Topics

| Topic | Purpose | Partitions | Replication | Retention |
|-------|---------|------------|-------------|-----------|
| `user-created-events` | Published when a new user is created | 3 | 2 | 7 days |
| `user-updated-events` | Published when user information is updated | 3 | 2 | 7 days |
| `user-deleted-events` | Published when a user is deleted | 3 | 2 | 7 days |
| `email-verification-events` | Published when email verification is required | 3 | 2 | 7 days |
| `*-dlq` | Dead Letter Queue for failed messages | 3 | 2 | 30 days |

### Event Ordering and Concurrency

- **Partition by userId**: All events for the same user go to the same partition
- **Sequential processing per partition**: Kafka guarantees order within a partition
- **Parallel processing across partitions**: Different users can be processed concurrently
- **3 consumer threads**: Matches partition count for optimal throughput

## Configuration

### Docker Kafka Cluster

Your Kafka cluster has 3 brokers running in Docker:
- `kafka-0`: Port 9094
- `kafka-1`: Port 9095
- `kafka-2`: Port 9096

### Application Configuration

Located in `application.yaml`:

```yaml
kafka:
  enabled: false  # Set to true to enable Kafka
  bootstrap-servers: localhost:9094,localhost:9095,localhost:9096
```

## Enabling Kafka

### 1. Start Kafka Cluster

```bash
docker-compose -f docker-compose.dev.yml up -d kafka-0 kafka-1 kafka-2
```

### 2. Verify Kafka is Running

```bash
docker ps | grep kafka
```

You should see 3 Kafka containers running.

### 3. Enable Kafka in Application

In `application.yaml`, set:

```yaml
kafka:
  enabled: true
```

### 4. Start the Microservice

```bash
mvn spring-boot:run
```

## Using Kafka

### Publishing Events

Events are automatically published when you use the domain services. For example:

```java
// In UserApplicationService or similar
User user = User.create("john", "john@example.com", passwordHash);
userRepository.save(user);

// Publish event (if Kafka is enabled)
eventPublisher.publishUserCreated(user);
```

### Consuming Events

Consumers are automatically active when Kafka is enabled. They will:
1. Check if event was already processed (idempotency)
2. Process the event
3. Mark as processed in database
4. Acknowledge to Kafka

### Monitoring Events

#### View Topics

```bash
docker exec -it kafka-0 /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --list
```

#### Consume Events from Topic

```bash
docker exec -it kafka-0 /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic user-created-events \
  --from-beginning
```

#### View Consumer Groups

```bash
docker exec -it kafka-0 /opt/kafka/bin/kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --list
```

#### Check Consumer Lag

```bash
docker exec -it kafka-0 /opt/kafka/bin/kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --group identity-service-group \
  --describe
```

## Idempotency

The system ensures idempotent processing:

1. Each event has a unique `eventId` (UUID)
2. Before processing, the consumer checks if `eventId` exists in `processed_events` table
3. If exists, the event is skipped (duplicate)
4. If not exists, the event is processed and marked as processed

### Database Table

```sql
CREATE TABLE processed_events (
    event_id VARCHAR(100) PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    partition_id INTEGER NOT NULL,
    offset_value BIGINT NOT NULL,
    processed_at TIMESTAMP NOT NULL
);
```

## Dead Letter Queue (DLQ)

Failed events are sent to DLQ topics:

- `user-created-events-dlq`
- `user-updated-events-dlq`
- `user-deleted-events-dlq`
- `email-verification-events-dlq`

### Viewing DLQ Events

```bash
docker exec -it kafka-0 /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic user-created-events-dlq \
  --from-beginning
```

### Reprocessing DLQ Events

1. Identify the failed event in DLQ
2. Fix the underlying issue (code bug, external service, etc.)
3. Manually republish the event to the main topic
4. The consumer will process it again

## Troubleshooting

### Kafka Not Starting

Check Docker logs:
```bash
docker logs kafka-0
docker logs kafka-1
docker logs kafka-2
```

### Events Not Being Consumed

1. Check if Kafka is enabled: `kafka.enabled=true`
2. Check consumer logs for errors
3. Check consumer lag (see above)
4. Verify topics exist

### Duplicate Events

This is expected with at-least-once delivery. The idempotency mechanism handles duplicates automatically.

### Consumer Lag Growing

1. Check for slow processing in consumer logic
2. Consider increasing concurrency (more consumer threads)
3. Consider adding more partitions (requires rebalancing)

## Performance Tuning

### Producer Settings

In `application.yaml`:

```yaml
kafka:
  producer:
    batch-size: 16384  # Increase for higher throughput
    linger-ms: 10      # Increase to batch more messages
    compression-type: snappy  # Use snappy for good compression/speed balance
```

### Consumer Settings

```yaml
kafka:
  consumer:
    max-poll-records: 10  # Increase to process more messages per poll
    fetch-min-size: 1     # Increase to reduce network calls
  listener:
    concurrency: 3  # Match partition count
```

## Best Practices

1. **Always use userId as partition key** for user events (maintains ordering)
2. **Don't include sensitive data** in events (e.g., passwords)
3. **Keep events small** (< 1MB)
4. **Monitor consumer lag** regularly
5. **Clean up old processed events** periodically
6. **Test DLQ handling** in staging environment
7. **Use structured logging** with event IDs for tracing

## Cleanup

### Delete Old Processed Events

Events older than 30 days can be cleaned up:

```java
Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
processedEventRepository.deleteByProcessedAtBefore(thirtyDaysAgo);
```

Consider scheduling this as a cron job.

## Next Steps

1. Implement email service integration in `EmailVerificationConsumer`
2. Add business logic in consumer processing methods
3. Create integration tests with `@EmbeddedKafka`
4. Set up monitoring with Prometheus and Grafana
5. Configure alerts for consumer lag
