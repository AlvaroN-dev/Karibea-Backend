package com.microservice.chatbot.infrastructure.controller;

import com.microservice.chatbot.application.dto.*;
import com.microservice.chatbot.application.mapper.ConversationMapper;
import com.microservice.chatbot.domain.models.ChatSource;
import com.microservice.chatbot.domain.models.Conversation;
import com.microservice.chatbot.domain.models.Escalation;
import com.microservice.chatbot.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Chat operations.
 * Location: infrastructure/controller - REST API endpoint.
 * 
 * Endpoints:
 * - POST /api/chat/messages
 * - GET /api/chat/conversations/{id}
 * - POST /api/chat/conversations
 * - GET /api/chat/sources
 * - POST /api/chat/escalate
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Chat API endpoints for RAG chatbot")
public class ChatController {

    private final SendMessageUseCase sendMessageUseCase;
    private final GetConversationUseCase getConversationUseCase;
    private final CreateConversationUseCase createConversationUseCase;
    private final GetSourcesUseCase getSourcesUseCase;
    private final EscalateUseCase escalateUseCase;
    private final ConversationMapper conversationMapper;

    /**
     * POST /api/chat/messages - Send a message to the chatbot
     */
    @PostMapping("/messages")
    @Operation(summary = "Send message", description = "Send a message to the chatbot and receive AI response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Conversation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ChatApiResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        log.info("POST /messages - conversationId: {}", request.getConversationId());

        ChatApiResponse response = sendMessageUseCase.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/chat/conversations/{conversationId} - Get conversation by ID
     */
    @GetMapping("/conversations/{conversationId}")
    @Operation(summary = "Get conversation", description = "Retrieve a conversation with its message history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Conversation not found")
    })
    public ResponseEntity<ConversationResponse> getConversation(
            @PathVariable UUID conversationId) {
        log.info("GET /conversations/{}", conversationId);

        Conversation conversation = getConversationUseCase.getConversation(conversationId);
        ConversationResponse response = conversationMapper.toResponse(conversation);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/chat/conversations - Create a new conversation
     */
    @PostMapping("/conversations")
    @Operation(summary = "Create conversation", description = "Start a new chat conversation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ConversationResponse> createConversation(
            @Valid @RequestBody ConversationRequest request) {
        log.info("POST /conversations - channel: {}", request.getChannel());

        Conversation conversation = createConversationUseCase.createConversation(request);
        ConversationResponse response = conversationMapper.toResponse(conversation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/chat/sources - Get available sources for RAG
     */
    @GetMapping("/sources")
    @Operation(summary = "Get sources", description = "Retrieve available knowledge sources for the chatbot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sources retrieved successfully")
    })
    public ResponseEntity<List<SourceResponse>> getSources() {
        log.info("GET /sources");

        List<ChatSource> sources = getSourcesUseCase.getSources();
        List<SourceResponse> response = sources.stream()
                .map(source -> SourceResponse.builder()
                        .id(source.getId())
                        .type(source.getType().name())
                        .content(source.getContent())
                        .score(source.getScore())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/chat/escalate - Escalate conversation to human agent
     */
    @PostMapping("/escalate")
    @Operation(summary = "Escalate conversation", description = "Escalate a conversation to a human agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escalation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Conversation not found")
    })
    public ResponseEntity<EscalationResponse> escalate(
            @Valid @RequestBody EscalationRequest request) {
        log.info("POST /escalate - conversationId: {}", request.getConversationId());

        Escalation escalation = escalateUseCase.escalate(request);

        EscalationResponse response = EscalationResponse.builder()
                .id(escalation.getId())
                .conversationId(escalation.getConversationId())
                .reason(escalation.getReason())
                .priority(escalation.getPriority().name())
                .escalatedAt(escalation.getEscalatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
