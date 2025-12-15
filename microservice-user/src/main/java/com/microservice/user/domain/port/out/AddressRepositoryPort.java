package com.microservice.user.domain.port.out;

import com.microservice.user.domain.models.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto saliente - Repositorio de direcciones
 */
public interface AddressRepositoryPort {
    
    Address save(Address address);
    
    Optional<Address> findById(UUID id);
    
    List<Address> findByExternalUserId(UUID externalUserId);
    
    List<Address> findActiveByExternalUserId(UUID externalUserId);
    
    Optional<Address> findDefaultByExternalUserId(UUID externalUserId);
    
    void deleteById(UUID id);
    
    void removeDefaultByExternalUserId(UUID externalUserId);
}
