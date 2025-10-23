package com.aiticketmanager.integration;

import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
        log.info("classifyTicket() called for: title='{}'", title);

        if (mockEnabled) {
            log.warn("AI mock mode enabled – returning simulated classification");
            return new ClassificationResult(TicketCategory.OTHER, TicketPriority.MEDIUM);
        }

        String text = (title == null ? "" : title) + " -- " + (description == null ? "" : description);
        log.info("Calling Hugging Face model at {} (token present: {})", aiServiceUrl, apiToken != null);

        List<String> labels = List.of("SOFTWARE_BUG", "BILLING", "NETWORK", "ACCOUNT_ACCESS", "OTHER");
        Map<String, Object> requestBody = Map.of(
                "inputs", text,
                "parameters", Map.of("candidate_labels", labels)
        );

        try {
            log.debug("➡Sending request body: {}", requestBody);

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

            log.debug("Hugging Face raw response: {}", response);

            @SuppressWarnings("unchecked")
            List<String> outLabels = (List<String>) response.get("labels");
            @SuppressWarnings("unchecked")
            List<Number> scores = (List<Number>) response.get("scores");

            if (outLabels == null || outLabels.isEmpty()) {
                log.warn("Hugging Face returned no labels — falling back to default classification");
                return new ClassificationResult(TicketCategory.OTHER, TicketPriority.MEDIUM);
            }

            String top = outLabels.get(0);
            double topScore = (scores != null && !scores.isEmpty()) ? scores.get(0).doubleValue() : 0.5;

            TicketCategory category = toCategory(top);
            TicketPriority priority = toPriority(topScore, category, text);

            log.info("Classified as Category={}, Priority={}", category, priority);

            return new ClassificationResult(category, priority);

        } catch (Exception e) {
            log.error("AI classification failed: {}", e.getMessage());
            return new ClassificationResult(TicketCategory.OTHER, TicketPriority.MEDIUM);
        }
    }

    private TicketCategory toCategory(String label) {
        if (label == null) return TicketCategory.OTHER;
        String normalized = label.trim().toUpperCase().replace(' ', '_');
        try {
            return TicketCategory.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
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
        String lc = text.toLowerCase();
        boolean urgentKeywords = lc.contains("cannot") || lc.contains("locked") || lc.contains("down") || lc.contains("crash");

        if (confidence >= 0.90 || urgentKeywords) return TicketPriority.HIGH;
        if (confidence >= 0.60) return TicketPriority.MEDIUM;
        return TicketPriority.LOW;
    }
}
