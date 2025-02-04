package com.tarmac.ollamaservice.controller;

import com.tarmac.ollamaservice.config.ChatGPTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping
    public ResponseEntity<?> chat(@RequestParam String prompt) {
        String response = chatGPTService.generateChatResponse(prompt);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
