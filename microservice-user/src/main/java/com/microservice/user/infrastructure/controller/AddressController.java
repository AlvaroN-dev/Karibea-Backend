package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.request.CreateAddressRequest;
import com.microservice.user.application.dto.request.UpdateAddressRequest;
import com.microservice.user.application.dto.response.AddressResponse;
import com.microservice.user.application.mapper.AddressDtoMapper;
import com.microservice.user.domain.exceptions.AddressNotFoundException;
import com.microservice.user.domain.models.Address;
import com.microservice.user.domain.port.in.ManageAddressUseCase;
import com.microservice.user.domain.port.in.ManageAddressUseCase.CreateAddressCommand;
import com.microservice.user.domain.port.in.ManageAddressUseCase.UpdateAddressCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para direcciones
 */
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {
    
    private final ManageAddressUseCase manageAddressUseCase;
    private final AddressDtoMapper mapper;
    
    public AddressController(ManageAddressUseCase manageAddressUseCase, 
                            AddressDtoMapper mapper) {
        this.manageAddressUseCase = manageAddressUseCase;
        this.mapper = mapper;
    }
    
    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {
        CreateAddressCommand command = new CreateAddressCommand(
            request.externalUserId(),
            request.label(),
            request.streetAddress(),
            request.city(),
            request.state(),
            request.postalCode(),
            request.country(),
            request.isDefault()
        );
        
        Address address = manageAddressUseCase.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(address));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable UUID id) {
        Address address = manageAddressUseCase.findById(id)
            .orElseThrow(() -> new AddressNotFoundException(id));
        return ResponseEntity.ok(mapper.toResponse(address));
    }
    
    @GetMapping("/user/{externalUserId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByUser(
            @PathVariable UUID externalUserId) {
        List<Address> addresses = manageAddressUseCase.findByExternalUserId(externalUserId);
        return ResponseEntity.ok(mapper.toResponseList(addresses));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAddressRequest request) {
        UpdateAddressCommand command = new UpdateAddressCommand(
            id,
            request.label(),
            request.streetAddress(),
            request.city(),
            request.state(),
            request.postalCode(),
            request.country()
        );
        
        Address address = manageAddressUseCase.update(command);
        return ResponseEntity.ok(mapper.toResponse(address));
    }
    
    @PatchMapping("/{id}/default")
    public ResponseEntity<Void> setAsDefault(
            @PathVariable UUID id,
            @RequestParam UUID externalUserId) {
        manageAddressUseCase.setAsDefault(id, externalUserId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        manageAddressUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
