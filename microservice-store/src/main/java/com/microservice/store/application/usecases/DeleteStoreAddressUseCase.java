package com.microservice.store.application.usecases;

import com.microservice.store.domain.service.StoreAddressService;
import java.util.UUID;

public class DeleteStoreAddressUseCase {

    private final StoreAddressService service;

    public DeleteStoreAddressUseCase(StoreAddressService service) {
        this.service = service;
    }

    public void execute(UUID id) {
        service.delete(id);
    }
}
