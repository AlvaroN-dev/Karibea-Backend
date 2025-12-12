package com.microservice.order.domain.ports.in;

public interface DeleteOrderUseCase {
    Boolean deleteOrderById(Long id);
}
