package com.example.contract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.ChatModel;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.response.ChatResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AiServiceTest {

    @Mock
    private ChatModel chatModel;

    @InjectMocks
    private AiService aiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void extractClauses_returnsListFromMockedResponse() {
        String mockOutput = "Clause one\nClause two\nClause three";
        ChatResponse mockResponse = new ChatResponse(new AssistantMessage(mockOutput), List.of());
        when(chatModel.call(any(UserMessage.class))).thenReturn(mockResponse);
        List<String> clauses = aiService.extractClauses("dummy contract text");
        assertEquals(3, clauses.size());
        assertTrue(clauses.contains("Clause one"));
        assertTrue(clauses.contains("Clause two"));
        assertTrue(clauses.contains("Clause three"));
    }
}
