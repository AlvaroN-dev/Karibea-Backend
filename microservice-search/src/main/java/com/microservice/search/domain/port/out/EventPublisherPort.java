package com.microservice.search.domain.port.out;

import com.microservice.search.domain.events.DomainEvent;

/**
 * Puerto de salida para publicar eventos de dominio a Kafka.
 */
public interface EventPublisherPort {

    /**
     * Publica un evento de dominio.
     *
     * @param event evento a publicar
     */
    void publish(DomainEvent event);

    /**
     * Publica un evento a un topic específico.
     *
     * @param topic nombre del topic
     * @param event evento a publicar
     */
    void publish(String topic, DomainEvent event);
}
