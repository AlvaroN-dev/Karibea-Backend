package com.microservice.chatbot.application.mapper;

import com.microservice.chatbot.application.dto.ConversationResponse;
import com.microservice.chatbot.domain.models.Conversation;
import com.microservice.chatbot.domain.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for Conversation domain model to DTO conversion.
 * Location: application/mapper - MapStruct mapper.
 */
@Mapper(componentModel = "spring")
public interface ConversationMapper {

    @Mapping(target = "status", source = ".", qualifiedByName = "mapStatus")
    @Mapping(target = "messageCount", expression = "java(conversation.getMessages() != null ? conversation.getMessages().size() : 0)")
    @Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessages")
    @Mapping(target = "userInfo", source = "userInfo")
    ConversationResponse toResponse(Conversation conversation);

    @Named("mapStatus")
    default String mapStatus(Conversation conversation) {
        if (conversation.isDeleted()) {
            return "DELETED";
        }
        if (conversation.getEndedAt() != null) {
            return "ENDED";
        }
        return "ACTIVE";
    }

    @Named("mapMessages")
    default List<ConversationResponse.MessageResponse> mapMessages(List<Message> messages) {
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .map(this::mapMessage)
                .collect(Collectors.toList());
    }

    default ConversationResponse.MessageResponse mapMessage(Message message) {
        if (message == null) {
            return null;
        }
        return ConversationResponse.MessageResponse.builder()
                .id(message.getId())
                .senderType(message.getSenderType() != null ? message.getSenderType().name() : null)
                .content(message.getContent())
                .intent(message.getIntent())
                .confidence(message.getConfidence() != null ? message.getConfidence().doubleValue() : null)
                .read(message.isRead())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
