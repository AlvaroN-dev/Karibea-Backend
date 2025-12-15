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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for address management.
 */
@RestController
@RequestMapping("/api/v1/addresses")
@Tag(name = "Addresses", description = "Operations for managing user addresses")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {
    
    private final ManageAddressUseCase manageAddressUseCase;
    private final AddressDtoMapper mapper;
    
    public AddressController(ManageAddressUseCase manageAddressUseCase, 
                            AddressDtoMapper mapper) {
        this.manageAddressUseCase = manageAddressUseCase;
        this.mapper = mapper;
    }
    
    @PostMapping
    @Operation(summary = "Create address", description = "Creates a new address for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Address created successfully",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {
        CreateAddressCommand command = new CreateAddressCommand(
            request.getExternalUserId(),
            request.getLabel(),
            request.getStreetAddress(),
            request.getCity(),
            request.getState(),
            request.getPostalCode(),
            request.getCountry(),
            request.isDefault()
        );
        
        Address address = manageAddressUseCase.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(address));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get address by ID", description = "Retrieves an address by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address found",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressResponse> getAddressById(
            @Parameter(description = "Address ID") @PathVariable UUID id) {
        Address address = manageAddressUseCase.findById(id)
            .orElseThrow(() -> new AddressNotFoundException(id));
        return ResponseEntity.ok(mapper.toResponse(address));
    }
    
    @GetMapping("/user/{externalUserId}")
    @Operation(summary = "Get user addresses", description = "Retrieves all addresses for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressResponse.class)))),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<AddressResponse>> getAddressesByUser(
            @Parameter(description = "External user ID") @PathVariable UUID externalUserId) {
        List<Address> addresses = manageAddressUseCase.findByExternalUserId(externalUserId);
        return ResponseEntity.ok(mapper.toResponseList(addresses));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update address", description = "Updates an existing address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address updated successfully",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressResponse> updateAddress(
            @Parameter(description = "Address ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateAddressRequest request) {
        UpdateAddressCommand command = new UpdateAddressCommand(
            id,
            request.getLabel(),
            request.getStreetAddress(),
            request.getCity(),
            request.getState(),
            request.getPostalCode(),
            request.getCountry()
        );
        
        Address address = manageAddressUseCase.update(command);
        return ResponseEntity.ok(mapper.toResponse(address));
    }
    
    @PatchMapping("/{id}/default")
    @Operation(summary = "Set default address", description = "Sets an address as the user's default")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Default address set successfully"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<Void> setAsDefault(
            @Parameter(description = "Address ID") @PathVariable UUID id,
            @Parameter(description = "External user ID") @RequestParam UUID externalUserId) {
        manageAddressUseCase.setAsDefault(id, externalUserId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete address", description = "Deletes an address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "Address ID") @PathVariable UUID id) {
        manageAddressUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
