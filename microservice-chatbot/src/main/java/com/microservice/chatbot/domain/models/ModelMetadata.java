package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value object representing AI model metadata.
 * Location: domain/models - Value object for model information.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelMetadata {

    private String provider;
    private String modelName;
    private String version;
    private int maxTokens;
    private double temperature;
    private boolean supportsStreaming;

    /**
     * Creates metadata for AI21 provider.
     */
    public static ModelMetadata ai21(String modelName, int maxTokens, double temperature) {
        return ModelMetadata.builder()
                .provider("AI21")
                .modelName(modelName)
                .version("1.0")
                .maxTokens(maxTokens)
                .temperature(temperature)
                .supportsStreaming(false)
                .build();
    }

    /**
     * Creates metadata for OpenAI provider.
     */
    public static ModelMetadata openAI(String modelName, int maxTokens, double temperature) {
        return ModelMetadata.builder()
                .provider("OpenAI")
                .modelName(modelName)
                .version("1.0")
                .maxTokens(maxTokens)
                .temperature(temperature)
                .supportsStreaming(true)
                .build();
    }

    /**
     * Creates metadata for stub/test provider.
     */
    public static ModelMetadata stub() {
        return ModelMetadata.builder()
                .provider("Stub")
                .modelName("test-model")
                .version("1.0")
                .maxTokens(100)
                .temperature(0.0)
                .supportsStreaming(false)
                .build();
    }
}
