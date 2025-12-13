package com.microservice.chatbot.application.usecases;

import com.microservice.chatbot.domain.models.ChatSource;
import com.microservice.chatbot.domain.port.in.GetSourcesUseCase;
import com.microservice.chatbot.domain.port.out.RetrieverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of GetSourcesUseCase.
 * Location: application/usecases - Use case implementation.
 */
@Service
@RequiredArgsConstructor
public class GetSourcesUseCaseImpl implements GetSourcesUseCase {

    private final RetrieverService retrieverService;

    @Override
    public List<ChatSource> getSources() {
        return retrieverService.getAllSources();
    }
}
