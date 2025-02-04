package com.tarmac.ollamaservice.controller;

import com.tarmac.ollamaservice.config.OllamaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class OllamaController {

    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/generate")
    public String generate(@RequestParam String model, @RequestParam String prompt) {
        return ollamaService.generateResponse(model, prompt);
    }

    @PostMapping("/generate/default")
    public String generateDefault(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return ollamaService.generateResponseDefault(prompt);
    }

    @PostMapping("/generate/deepseek")
    public String generateDeepSeek(@RequestParam String prompt) {
        return ollamaService.generateResponseDeepSeek(prompt);
    }
}
