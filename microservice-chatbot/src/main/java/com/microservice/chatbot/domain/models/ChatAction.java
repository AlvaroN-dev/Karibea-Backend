package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Value object representing an action suggested by the AI.
 * Location: domain/models - Value object for AI-suggested actions.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAction {

    private ActionType type;
    private Map<String, Object> payload;

    /**
     * Enum representing the type of action.
     */
    public enum ActionType {
        OPEN_URL,
        ADD_TO_CART,
        ESCALATE,
        SHOW_PRODUCT,
        TRACK_ORDER,
        NONE
    }

    /**
     * Creates an open URL action.
     */
    public static ChatAction openUrl(String url) {
        return ChatAction.builder()
                .type(ActionType.OPEN_URL)
                .payload(Map.of("url", url))
                .build();
    }

    /**
     * Creates an add to cart action.
     */
    public static ChatAction addToCart(String productId, int quantity) {
        return ChatAction.builder()
                .type(ActionType.ADD_TO_CART)
                .payload(Map.of("productId", productId, "quantity", quantity))
                .build();
    }

    /**
     * Creates an escalate action.
     */
    public static ChatAction escalate(String reason) {
        return ChatAction.builder()
                .type(ActionType.ESCALATE)
                .payload(Map.of("reason", reason))
                .build();
    }

    /**
     * Creates a no-action response.
     */
    public static ChatAction none() {
        return ChatAction.builder()
                .type(ActionType.NONE)
                .payload(Map.of())
                .build();
    }
}
