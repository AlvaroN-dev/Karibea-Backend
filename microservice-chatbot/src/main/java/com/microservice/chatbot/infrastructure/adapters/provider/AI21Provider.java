package com.microservice.chatbot.infrastructure.adapters.provider;

import com.microservice.chatbot.application.dto.ChatApiResponse;
import com.microservice.chatbot.application.services.ResponseNormalizerService;
import com.microservice.chatbot.domain.exceptions.AIProviderException;
import com.microservice.chatbot.domain.models.ContextPayload;
import com.microservice.chatbot.domain.models.ModelMetadata;
import com.microservice.chatbot.domain.port.out.IAIProvider;
import org.springframework.core.ParameterizedTypeReference;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * AI21 Studio implementation of IAIProvider.
 * Location: infrastructure/adapters/provider - External AI provider adapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ai.provider", havingValue = "ai21", matchIfMissing = true)
public class AI21Provider implements IAIProvider {

    private final RestTemplate restTemplate;
    private final ResponseNormalizerService responseNormalizer;

    @Value("${ai.ai21.api.key:}")
    private String apiKey;

    @Value("${ai.ai21.api.url:https://api.ai21.com/studio/v1/chat/completions}")
    private String apiUrl;

    @Value("${ai.ai21.api.model:jamba-mini-1.7}")
    private String model;

    @Value("${ai.ai21.max-tokens:300}")
    private int maxTokens;

    @Value("${ai.ai21.temperature:0.7}")
    private double temperature;

    @Override
    @CircuitBreaker(name = "aiProvider", fallbackMethod = "fallbackResponse")
    @RateLimiter(name = "aiProvider")
    public ChatApiResponse generateResponse(ContextPayload contextPayload) {
        log.info("Generating response with AI21 provider");

        if (!isConfigured()) {
            throw new AIProviderException("AI21", "API key not configured");
        }

        try {
            HttpHeaders headers = buildHeaders();
            Map<String, Object> requestBody = buildRequestBody(contextPayload);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response.getBody() == null) {
                throw new AIProviderException("AI21", "Empty response from API");
            }

            String content = extractContent(response.getBody());
            return responseNormalizer.normalize(content);

        } catch (AIProviderException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error calling AI21 API: {}", e.getMessage(), e);
            throw new AIProviderException("AI21", "Failed to generate response: " + e.getMessage(), e);
        }
    }

    @Override
    public Flux<String> streamResponse(ContextPayload contextPayload) {
        // AI21 streaming not implemented - return simple mono
        log.warn("Streaming not supported by AI21 provider, using synchronous response");
        return Flux.just(generateResponse(contextPayload).getAnswerText());
    }

    @Override
    public ModelMetadata getModelMetadata() {
        return ModelMetadata.ai21(model, maxTokens, temperature);
    }

    @Override
    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() &&
                apiUrl != null && !apiUrl.trim().isEmpty();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.set("User-Agent", "Karibea-Chatbot/1.0");
        return headers;
    }

    private Map<String, Object> buildRequestBody(ContextPayload contextPayload) {
        return Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", contextPayload.getSystemPrompt()),
                        Map.of("role", "user", "content", contextPayload.getFormattedPrompt())),
                "max_tokens", maxTokens,
                "temperature", temperature);
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
            return "Unable to extract response content";
        } catch (Exception e) {
            log.error("Error extracting content from response: {}", e.getMessage());
            return "Error processing response";
        }
    }

    @SuppressWarnings("unused")
    private ChatApiResponse fallbackResponse(ContextPayload contextPayload, Exception e) {
        log.warn("AI21 provider fallback triggered: {}", e.getMessage());
        return ChatApiResponse.simpleResponse(
                "I'm experiencing some difficulties at the moment. Please try again in a few seconds.");
    }
}
