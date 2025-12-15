package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.response.CurrencyResponse;
import com.microservice.user.application.dto.response.GenderResponse;
import com.microservice.user.application.dto.response.LanguageResponse;
import com.microservice.user.domain.port.out.CurrencyRepositoryPort;
import com.microservice.user.domain.port.out.GenderRepositoryPort;
import com.microservice.user.domain.port.out.LanguageRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for catalog data (public endpoints).
 */
@RestController
@RequestMapping("/api/v1/catalogs")
@Tag(name = "Catalogs", description = "Public catalog data for genders, currencies, and languages")
public class CatalogController {
    
    private final GenderRepositoryPort genderRepository;
    private final CurrencyRepositoryPort currencyRepository;
    private final LanguageRepositoryPort languageRepository;
    
    public CatalogController(GenderRepositoryPort genderRepository,
                            CurrencyRepositoryPort currencyRepository,
                            LanguageRepositoryPort languageRepository) {
        this.genderRepository = genderRepository;
        this.currencyRepository = currencyRepository;
        this.languageRepository = languageRepository;
    }
    
    @GetMapping("/genders")
    @Operation(summary = "Get all genders", description = "Retrieves all available gender options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Genders retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenderResponse.class))))
    })
    public ResponseEntity<List<GenderResponse>> getAllGenders() {
        List<GenderResponse> genders = genderRepository.findAll().stream()
            .map(g -> new GenderResponse(g.getId(), g.getName()))
            .toList();
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/currencies")
    @Operation(summary = "Get all currencies", description = "Retrieves all available currency options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Currencies retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CurrencyResponse.class))))
    })
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        List<CurrencyResponse> currencies = currencyRepository.findAll().stream()
            .map(c -> new CurrencyResponse(c.getId(), c.getName(), c.getCode()))
            .toList();
        return ResponseEntity.ok(currencies);
    }
    
    @GetMapping("/languages")
    @Operation(summary = "Get all languages", description = "Retrieves all available language options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Languages retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LanguageResponse.class))))
    })
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {
        List<LanguageResponse> languages = languageRepository.findAll().stream()
            .map(l -> new LanguageResponse(l.getId(), l.getName(), l.getCode()))
            .toList();
        return ResponseEntity.ok(languages);
    }
}
