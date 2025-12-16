package com.microservice.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Controller for Circuit Breaker.
 * Provides fallback responses when downstream services are unavailable.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping(value = "/identity", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> identityFallback() {
        return createFallbackResponse("Identity Service", "Authentication service is temporarily unavailable");
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> userFallback() {
        return createFallbackResponse("User Service", "User service is temporarily unavailable");
    }

    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> catalogFallback() {
        return createFallbackResponse("Catalog Service", "Catalog service is temporarily unavailable");
    }

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> orderFallback() {
        return createFallbackResponse("Order Service", "Order service is temporarily unavailable");
    }

    @GetMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> paymentFallback() {
        return createFallbackResponse("Payment Service", "Payment service is temporarily unavailable");
    }

    @GetMapping(value = "/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> inventoryFallback() {
        return createFallbackResponse("Inventory Service", "Inventory service is temporarily unavailable");
    }

    @GetMapping(value = "/shopcart", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> shopcartFallback() {
        return createFallbackResponse("Shopping Cart Service", "Shopping cart service is temporarily unavailable");
    }

    @GetMapping(value = "/marketing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> marketingFallback() {
        return createFallbackResponse("Marketing Service", "Marketing service is temporarily unavailable");
    }

    @GetMapping(value = "/shipping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> shippingFallback() {
        return createFallbackResponse("Shipping Service", "Shipping service is temporarily unavailable");
    }

    @GetMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> notificationFallback() {
        return createFallbackResponse("Notification Service", "Notification service is temporarily unavailable");
    }

    @GetMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> reviewFallback() {
        return createFallbackResponse("Review Service", "Review service is temporarily unavailable");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> searchFallback() {
        return createFallbackResponse("Search Service", "Search service is temporarily unavailable");
    }

    @GetMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> storeFallback() {
        return createFallbackResponse("Store Service", "Store service is temporarily unavailable");
    }

    @GetMapping(value = "/chatbot", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> chatbotFallback() {
        return createFallbackResponse("Chatbot Service", "Chatbot service is temporarily unavailable");
    }

    /**
     * Generic fallback endpoint
     */
    @GetMapping(value = "/generic", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> genericFallback() {
        return createFallbackResponse("Service", "The requested service is temporarily unavailable");
    }

    /**
     * Creates a standardized fallback response
     */
    private Mono<ResponseEntity<Map<String, Object>>> createFallbackResponse(String serviceName, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("service", serviceName);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("suggestion", "Please try again later or contact support if the issue persists");
        
        return Mono.just(ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response));
    }
}
