package com.microservice.order.domain.ports.out;

public interface ExternalProductClient {
    ExternalProduct getById(Long productId);
}
