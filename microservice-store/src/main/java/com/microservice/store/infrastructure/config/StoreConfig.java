package com.microservice.store.infrastructure.config;

import com.microservice.store.application.usecases.CreateStoreUseCase;
import com.microservice.store.domain.port.StoreRepositoryPort;
import com.microservice.store.domain.service.StoreService;
import com.microservice.store.infrastructure.adapters.StoreRepositoryAdapter;
import com.microservice.store.infrastructure.repositories.StoreJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {

    @Bean
    StoreRepositoryPort storeRepositoryPort(StoreJpaRepository repo) {
        return new StoreRepositoryAdapter(repo);
    }

    @Bean
    StoreService storeService(StoreRepositoryPort port) {
        return new StoreService(port);
    }

    @Bean
    CreateStoreUseCase createStoreUseCase(StoreService service) {
        return new CreateStoreUseCase(service);
    }
}
