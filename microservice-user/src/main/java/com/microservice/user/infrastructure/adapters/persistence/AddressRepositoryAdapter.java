package com.microservice.user.infrastructure.adapters.persistence;

import com.microservice.user.domain.models.Address;
import com.microservice.user.domain.port.out.AddressRepositoryPort;
import com.microservice.user.infrastructure.entities.AddressEntity;
import com.microservice.user.infrastructure.entities.mapper.AddressEntityMapper;
import com.microservice.user.infrastructure.repositories.JpaAddressRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para direcciones
 */
@Component
public class AddressRepositoryAdapter implements AddressRepositoryPort {
    
    private final JpaAddressRepository jpaRepository;
    private final AddressEntityMapper mapper;
    
    public AddressRepositoryAdapter(JpaAddressRepository jpaRepository,
                                    AddressEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Address save(Address address) {
        AddressEntity entity = mapper.toEntity(address);
        AddressEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Address> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Address> findByExternalUserId(UUID externalUserId) {
        return mapper.toDomainList(jpaRepository.findByExternalUserId(externalUserId));
    }
    
    @Override
    public List<Address> findActiveByExternalUserId(UUID externalUserId) {
        return mapper.toDomainList(jpaRepository.findByExternalUserIdAndDeletedFalse(externalUserId));
    }
    
    @Override
    public Optional<Address> findDefaultByExternalUserId(UUID externalUserId) {
        return jpaRepository.findByExternalUserIdAndIsDefaultTrueAndDeletedFalse(externalUserId)
            .map(mapper::toDomain);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public void removeDefaultByExternalUserId(UUID externalUserId) {
        jpaRepository.removeDefaultByExternalUserId(externalUserId);
    }
}
