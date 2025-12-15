package com.microservice.user.domain.port.in;

import com.microservice.user.domain.models.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto entrante - Caso de uso para gestionar direcciones
 */
public interface ManageAddressUseCase {
    
    Address create(CreateAddressCommand command);
    
    Address update(UpdateAddressCommand command);
    
    void delete(UUID addressId);
    
    Optional<Address> findById(UUID addressId);
    
    List<Address> findByExternalUserId(UUID externalUserId);
    
    void setAsDefault(UUID addressId, UUID externalUserId);
    
    record CreateAddressCommand(
        UUID externalUserId,
        String label,
        String streetAddress,
        String city,
        String state,
        String postalCode,
        String country,
        boolean isDefault
    ) {
        public CreateAddressCommand {
            if (externalUserId == null) {
                throw new IllegalArgumentException("External user ID is required");
            }
            if (streetAddress == null || streetAddress.isBlank()) {
                throw new IllegalArgumentException("Street address is required");
            }
            if (city == null || city.isBlank()) {
                throw new IllegalArgumentException("City is required");
            }
            if (country == null || country.isBlank()) {
                throw new IllegalArgumentException("Country is required");
            }
        }
    }
    
    record UpdateAddressCommand(
        UUID addressId,
        String label,
        String streetAddress,
        String city,
        String state,
        String postalCode,
        String country
    ) {
        public UpdateAddressCommand {
            if (addressId == null) {
                throw new IllegalArgumentException("Address ID is required");
            }
        }
    }
}
