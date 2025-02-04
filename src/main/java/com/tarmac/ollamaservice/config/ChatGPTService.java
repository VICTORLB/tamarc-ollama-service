package com.tarmac.ollamaservice.config;

import com.tarmac.ollamaservice.exception.ChatGPTServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatGPTService.class);

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model.default}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateChatResponse(String prompt) {
        try {
            // Monta o corpo da requisição
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", "You are a helpful assistant."),
                    Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("max_tokens", 500);

            // Configura os headers da requisição
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Debug: Log credentials (somente para ambiente de desenvolvimento)
            LOGGER.debug("Debug - API Key: {}", apiKey);
            LOGGER.debug("Debug - Model: {}", model);
            LOGGER.debug("Debug - Prompt: {}", prompt);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, Map.class);

            // Verifica se a resposta foi bem-sucedida e contém o corpo
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object choicesObj = response.getBody().get("choices");
                if (choicesObj instanceof List<?> choicesList && !choicesList.isEmpty()) {
                    Object firstChoice = choicesList.get(0);
                    if (firstChoice instanceof Map) {
                        Map<?, ?> firstChoiceMap = (Map<?, ?>) firstChoice;
                        Object messageObj = firstChoiceMap.get("message");
                        if (messageObj instanceof Map) {
                            Map<?, ?> messageMap = (Map<?, ?>) messageObj;
                            Object contentObj = messageMap.get("content");
                            if (contentObj != null) {
                                return contentObj.toString();
                            } else {
                                LOGGER.error("Message content is null in the OpenAI response.");
                                throw new ChatGPTServiceException("Message content is null in the OpenAI response.", HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                        } else {
                            LOGGER.error("The 'message' object is missing or has an invalid type in the OpenAI response.");
                            throw new ChatGPTServiceException("The 'message' object is missing or has an invalid type in the OpenAI response.", HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        LOGGER.error("The first choice is not a valid object in the OpenAI response.");
                        throw new ChatGPTServiceException("The first choice is not a valid object in the OpenAI response.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    LOGGER.error("The 'choices' list is empty or missing in the OpenAI response.");
                    throw new ChatGPTServiceException("The 'choices' list is empty or missing in the OpenAI response.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                LOGGER.error("Unsuccessful response from OpenAI. Status: {}. Body: {}", response.getStatusCode(), response.getBody());
                throw new ChatGPTServiceException("Failed to get a valid response from OpenAI.", (HttpStatus) response.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            LOGGER.error("HTTP error when calling the OpenAI API. Status: {}. Response body: {}",
                    ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw new ChatGPTServiceException("HTTP error when calling the OpenAI API.", ex, (HttpStatus) ex.getStatusCode());
        } catch (RestClientException ex) {
            LOGGER.error("Network error when calling the OpenAI API.", ex);
            throw new ChatGPTServiceException("Network error when calling the OpenAI API.", ex, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception ex) {
            LOGGER.error("Unexpected error when calling the OpenAI API.", ex);
            throw new ChatGPTServiceException("Unexpected error when calling the OpenAI API.", ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
