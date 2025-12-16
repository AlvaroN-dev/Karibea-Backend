package com.microservice.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Global Logging Filter for all requests passing through the Gateway.
 * Logs request details and response times for monitoring and debugging.
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = Instant.now().toEpochMilli();
        
        String requestId = java.util.UUID.randomUUID().toString().substring(0, 8);
        String method = request.getMethod().toString();
        String path = request.getURI().getPath();
        String remoteAddress = request.getRemoteAddress() != null 
            ? request.getRemoteAddress().getAddress().getHostAddress() 
            : "unknown";
        
        logger.info("[{}] Incoming request: {} {} from {}", 
            requestId, method, path, remoteAddress);
        
        // Add request ID to headers for tracing
        ServerHttpRequest mutatedRequest = request.mutate()
            .header("X-Request-ID", requestId)
            .header("X-Request-Start", String.valueOf(startTime))
            .build();
        
        return chain.filter(exchange.mutate().request(mutatedRequest).build())
            .then(Mono.fromRunnable(() -> {
                long duration = Instant.now().toEpochMilli() - startTime;
                int statusCode = exchange.getResponse().getStatusCode() != null 
                    ? exchange.getResponse().getStatusCode().value() 
                    : 0;
                
                if (statusCode >= 400) {
                    logger.warn("[{}] Completed: {} {} - Status: {} - Duration: {}ms", 
                        requestId, method, path, statusCode, duration);
                } else {
                    logger.info("[{}] Completed: {} {} - Status: {} - Duration: {}ms", 
                        requestId, method, path, statusCode, duration);
                }
            }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
