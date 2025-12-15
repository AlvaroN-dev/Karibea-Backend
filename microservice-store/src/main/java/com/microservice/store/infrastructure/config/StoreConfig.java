package com.microservice.store.infrastructure.config;

import com.microservice.store.application.usecases.CreateStoreUseCase;
import com.microservice.store.domain.port.StoreRepositoryPort;
import com.microservice.store.domain.service.StoreService;
import com.microservice.store.domain.port.UserGatewayPort;
import com.microservice.store.domain.port.StoreEventPublisherPort;
import com.microservice.store.infrastructure.adapters.StoreRepositoryAdapter;
import com.microservice.store.infrastructure.repositories.StoreJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@org.springframework.data.jpa.repository.config.EnableJpaAuditing
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
    CreateStoreUseCase createStoreUseCase(
            StoreService service,
            UserGatewayPort userGatewayPort,
            StoreEventPublisherPort storeEventPublisherPort,
            com.microservice.store.infrastructure.adapters.mapper.StoreEventMapper storeEventMapper) {
        return new CreateStoreUseCase(service, userGatewayPort, storeEventPublisherPort, storeEventMapper);
    }
}
