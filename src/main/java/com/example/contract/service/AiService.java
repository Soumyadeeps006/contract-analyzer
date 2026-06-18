package com.example.contract.service;

import org.springframework.ai.chat.ChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AiService {

    private final ChatModel chatModel;

    public AiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Extract key clauses from the plain text of a contract using an LLM.
     * Returns a list of clause strings.
     */
    public List<String> extractClauses(String documentText) {
        String prompt = "You are an AI contract analyst. Extract the most important clauses from the following contract text. " +
                "Return each clause as a separate line without any numbering or bullet characters.\n\n" +
                "Contract:\n" + documentText;
        var response = chatModel.call(new UserMessage(prompt));
        String content = response.getResult().getOutput().getContent();
        // Split lines and filter empty ones
        return Arrays.stream(content.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    /**
     * Evaluate extracted clauses against a set of compliance rules.
     * Returns a map where the key is the rule name and the value indicates compliance (true = compliant).
     */
    public Map<String, Boolean> evaluateCompliance(List<String> clauses) {
        // For demonstration we just perform keyword checks. In a real system you would use a more sophisticated LLM prompt.
        Map<String, Boolean> result = new HashMap<>();
        // Example rules – these could be externalized later.
        result.put("GDPR Data Retention", clauses.stream().anyMatch(c -> c.toLowerCase().contains("data retention")));
        result.put("SOC2 Access Control", clauses.stream().anyMatch(c -> c.toLowerCase().contains("access control")));
        result.put("Confidentiality", clauses.stream().anyMatch(c -> c.toLowerCase().contains("confidential")));
        return result;
    }
}
