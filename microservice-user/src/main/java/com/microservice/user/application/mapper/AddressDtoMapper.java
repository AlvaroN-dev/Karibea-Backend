package com.microservice.user.application.mapper;

import com.microservice.user.application.dto.response.AddressResponse;
import com.microservice.user.domain.models.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre dominio y DTOs para direcciones
 */
@Component
public class AddressDtoMapper {
    
    public AddressResponse toResponse(Address address) {
        if (address == null) {
            return null;
        }
        
        return new AddressResponse(
            address.getId(),
            address.getExternalUserId(),
            address.getLabel(),
            address.getStreetAddress(),
            address.getCity(),
            address.getState(),
            address.getPostalCode(),
            address.getCountry(),
            address.getFormattedAddress(),
            address.isDefault(),
            address.getCreatedAt(),
            address.getUpdatedAt()
        );
    }
    
    public List<AddressResponse> toResponseList(List<Address> addresses) {
        if (addresses == null) {
            return List.of();
        }
        return addresses.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}
