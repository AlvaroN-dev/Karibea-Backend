package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.StoreAddress;
import com.microservice.store.domain.service.StoreAddressService;

import java.util.List;

public class GetStoreAddressesUseCase {

    private final StoreAddressService service;

    public GetStoreAddressesUseCase(StoreAddressService service) {
        this.service = service;
    }

    public List<StoreAddress> execute(Long storeId) {
        return service.getAllByStoreId(storeId);
    }
}
