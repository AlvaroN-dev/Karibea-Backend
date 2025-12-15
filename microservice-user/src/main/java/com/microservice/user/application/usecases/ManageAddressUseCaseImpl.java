package com.microservice.user.application.usecases;

import com.microservice.user.domain.exceptions.AddressNotFoundException;
import com.microservice.user.domain.models.Address;
import com.microservice.user.domain.port.in.ManageAddressUseCase;
import com.microservice.user.domain.port.out.AddressRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del caso de uso para gestionar direcciones
 */
@Service
@Transactional
public class ManageAddressUseCaseImpl implements ManageAddressUseCase {
    
    private final AddressRepositoryPort addressRepository;
    
    public ManageAddressUseCaseImpl(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }
    
    @Override
    public Address create(CreateAddressCommand command) {
        Address address = Address.create(
            command.externalUserId(),
            command.label(),
            command.streetAddress(),
            command.city(),
            command.state(),
            command.postalCode(),
            command.country()
        );
        
        // Si es la dirección por defecto, quitar el flag de las demás
        if (command.isDefault()) {
            addressRepository.removeDefaultByExternalUserId(command.externalUserId());
            address.setAsDefault();
        }
        
        return addressRepository.save(address);
    }
    
    @Override
    public Address update(UpdateAddressCommand command) {
        Address address = addressRepository.findById(command.addressId())
            .orElseThrow(() -> new AddressNotFoundException(command.addressId()));
        
        address.update(
            command.label(),
            command.streetAddress(),
            command.city(),
            command.state(),
            command.postalCode(),
            command.country()
        );
        
        return addressRepository.save(address);
    }
    
    @Override
    public void delete(UUID addressId) {
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new AddressNotFoundException(addressId));
        
        address.markAsDeleted();
        addressRepository.save(address);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findById(UUID addressId) {
        return addressRepository.findById(addressId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByExternalUserId(UUID externalUserId) {
        return addressRepository.findActiveByExternalUserId(externalUserId);
    }
    
    @Override
    public void setAsDefault(UUID addressId, UUID externalUserId) {
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new AddressNotFoundException(addressId));
        
        // Quitar flag de las demás direcciones
        addressRepository.removeDefaultByExternalUserId(externalUserId);
        
        // Establecer como default
        address.setAsDefault();
        addressRepository.save(address);
    }
}
