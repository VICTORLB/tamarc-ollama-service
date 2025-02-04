package com.tarmac.ollamaservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OllamaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OllamaService.class);

    @Value("${ollama.api.url}")
    private String ollamaApiUrl;

    @Value("${ollama.model.default}")
    private String defaultModel;

    @Value("${ollama.model.deepseek}")
    private String deepseekModel;

    private final RestTemplate restTemplate;

    public OllamaService() {
        this.restTemplate = new RestTemplate();
    }

    public String generateResponse(String model, String prompt) {
        LOGGER.info("Generating response - Model: {}, Prompt: {}", model, prompt);
        return sendRequest(model, prompt);
    }

    public String generateResponseDefault(String prompt) {
        LOGGER.info("Generating response using Default model ({}) - Prompt: {}", defaultModel, prompt);
        return sendRequest(defaultModel, prompt);
    }

    public String generateResponseDeepSeek(String prompt) {
        LOGGER.info("Generating response using DeepSeek model ({}) - Prompt: {}", deepseekModel, prompt);
        return sendRequest(deepseekModel, prompt);
    }

    private String sendRequest(String model, String prompt) {
        String requestBody = buildRequestBody(model, prompt);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, buildHeaders());

        try {
            ResponseEntity<String> response = restTemplate.exchange(ollamaApiUrl, HttpMethod.POST, requestEntity, String.class);
            logResponse(response);
            return response.getBody();
        } catch (RestClientException e) {
            LOGGER.error("Error calling Ollama API", e.getMessage());
            return createErrorResponse("Failed to reach Ollama API");
        }
    }

    private String buildRequestBody(String model, String prompt) {
        return String.format("{\"model\": \"%s\", \"prompt\": \"%s\", \"stream\": false}", model, prompt);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private void logResponse(ResponseEntity<String> response) {
        LOGGER.info("Received response - Status: {}", response.getStatusCode());
        if (response.getBody() != null) {
            LOGGER.debug("Response Body: {}", response.getBody());
        }
    }

    private String createErrorResponse(String message) {
        return String.format("{\"error\": \"%s\"}", message);
    }
}
