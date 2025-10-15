package com.aiticketmanager.integration;

import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AIClient {

    private final WebClient webClient;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Value("${ai.api.token:}")
    private String apiToken;

    @Value("${ai.service.timeout:5s}")
    private Duration timeout;

    @Value("${ai.mock.enabled:false}")
    private boolean mockEnabled;

    public AIClient(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public record ClassificationResult(TicketCategory category, TicketPriority priority) {}

    public ClassificationResult classifyTicket(String title, String description) {
        if (mockEnabled) {
            // Deterministic result for tests/CI
            return new ClassificationResult(TicketCategory.OTHER, TicketPriority.MEDIUM);
        }

        String text = (title == null ? "" : title) + " -- " + (description == null ? "" : description);

        // Candidate labels should match your enum names or be mappable to them
        List<String> labels = List.of("SOFTWARE_BUG", "BILLING", "NETWORK", "ACCOUNT_ACCESS", "OTHER");

        Map<String, Object> requestBody = Map.of(
                "inputs", text,
                "parameters", Map.of("candidate_labels", labels)
        );

        try {
            Map<?, ?> response = webClient.post()
                    .uri(aiServiceUrl)
                    .headers(h -> {
                        if (apiToken != null && !apiToken.isBlank()) {
                            h.setBearerAuth(apiToken);
                        }
                    })
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(timeout)
                    .block();

            // Expected response: { "labels": [...], "scores": [...] }
            @SuppressWarnings("unchecked")
            List<String> outLabels = (List<String>) response.get("labels");
            @SuppressWarnings("unchecked")
            List<Number> scores = (List<Number>) response.get("scores");

            if (outLabels == null || outLabels.isEmpty()) {
                log.warn("HF returned no labels. Falling back.");
                return new ClassificationResult(TicketCategory.OTHER, TicketPriority.MEDIUM);
            }

            String top = outLabels.get(0);
            double topScore = scores != null && !scores.isEmpty() ? scores.get(0).doubleValue() : 0.5;

            TicketCategory category = toCategory(top);
            TicketPriority priority = toPriority(topScore, category, text);

            return new ClassificationResult(category, priority);

        } catch (Exception e) {
            log.error("AI classification failed: {}", e.getMessage());
            return new ClassificationResult(TicketCategory.OTHER, TicketPriority.MEDIUM);
        }
    }

    private TicketCategory toCategory(String label) {
        if (label == null) return TicketCategory.OTHER;
        // Normalize label to enum name
        String normalized = label.trim().toUpperCase().replace(' ', '_');
        try {
            return TicketCategory.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            // Basic mapping if HF returns different casing or synonyms
            return switch (normalized) {
                case "SOFTWARE", "BUG", "SOFTWARE_BUG" -> TicketCategory.SOFTWARE_BUG;
                case "BILLING", "PAYMENT" -> TicketCategory.BILLING;
                case "NETWORK", "CONNECTIVITY" -> TicketCategory.NETWORK;
                case "ACCOUNT", "ACCOUNT_ACCESS", "LOGIN" -> TicketCategory.ACCOUNT_ACCESS;
                default -> TicketCategory.OTHER;
            };
        }
    }

    private TicketPriority toPriority(double confidence, TicketCategory category, String text) {
        // Simple heuristic: high confidence or keywords → higher priority
        String lc = text.toLowerCase();
        boolean urgentKeywords = lc.contains("cannot") || lc.contains("locked") || lc.contains("down") || lc.contains("crash");

        if (confidence >= 0.90 || urgentKeywords) return TicketPriority.HIGH;
        if (confidence >= 0.60) return TicketPriority.MEDIUM;
        return TicketPriority.LOW;
    }
}
