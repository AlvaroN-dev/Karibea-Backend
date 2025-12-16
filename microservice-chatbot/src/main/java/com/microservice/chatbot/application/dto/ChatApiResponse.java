package com.microservice.chatbot.application.dto;

import com.microservice.chatbot.domain.models.ChatAction;
import com.microservice.chatbot.domain.models.ChatSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Standard RAG response format from the AI model.
 * Location: application/dto - Response DTO matching required output format.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatApiResponse {

    private String answerText;
    private List<SourceRef> sources;
    private List<ActionRef> actions;
    private IntentRef intent;

    /**
     * Source reference in the response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceRef {
        private String id;
        private String type;
        private BigDecimal score;

        public static SourceRef from(ChatSource source) {
            return SourceRef.builder()
                    .id(source.getId())
                    .type(source.getType().name().toLowerCase())
                    .score(source.getScore())
                    .build();
        }
    }

    /**
     * Action reference in the response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionRef {
        private String type;
        private Object payload;

        public static ActionRef from(ChatAction action) {
            return ActionRef.builder()
                    .type(action.getType().name().toLowerCase())
                    .payload(action.getPayload())
                    .build();
        }
    }

    /**
     * Intent reference in the response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntentRef {
        private String id;
        private BigDecimal confidence;

        public static IntentRef of(String id, BigDecimal confidence) {
            return IntentRef.builder()
                    .id(id)
                    .confidence(confidence)
                    .build();
        }
    }

    /**
     * Creates a simple text response with no sources or actions.
     */
    public static ChatApiResponse simpleResponse(String text) {
        return ChatApiResponse.builder()
                .answerText(text)
                .sources(List.of())
                .actions(List.of(ActionRef.builder().type("none").payload(new Object()).build()))
                .intent(IntentRef.of("unknown", BigDecimal.valueOf(0.5)))
                .build();
    }
}
