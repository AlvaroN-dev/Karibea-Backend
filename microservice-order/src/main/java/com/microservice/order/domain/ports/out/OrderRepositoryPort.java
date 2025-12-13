package com.microservice.order.domain.ports.out;

import com.microservice.order.domain.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order create(Order newOrder);
    Order getById(Long idOrder);

}
