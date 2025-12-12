package com.microservice.user.infrastructure.controller;

import com.microservice.user.application.dto.response.CurrencyResponse;
import com.microservice.user.application.dto.response.GenderResponse;
import com.microservice.user.application.dto.response.LanguageResponse;
import com.microservice.user.domain.port.out.CurrencyRepositoryPort;
import com.microservice.user.domain.port.out.GenderRepositoryPort;
import com.microservice.user.domain.port.out.LanguageRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para catálogos (géneros, monedas, idiomas)
 */
@RestController
@RequestMapping("/api/v1/catalogs")
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
    public ResponseEntity<List<GenderResponse>> getAllGenders() {
        List<GenderResponse> genders = genderRepository.findAll().stream()
            .map(g -> new GenderResponse(g.getId(), g.getName()))
            .toList();
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        List<CurrencyResponse> currencies = currencyRepository.findAll().stream()
            .map(c -> new CurrencyResponse(c.getId(), c.getName(), c.getCode()))
            .toList();
        return ResponseEntity.ok(currencies);
    }
    
    @GetMapping("/languages")
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {
        List<LanguageResponse> languages = languageRepository.findAll().stream()
            .map(l -> new LanguageResponse(l.getId(), l.getName(), l.getCode()))
            .toList();
        return ResponseEntity.ok(languages);
    }
}
