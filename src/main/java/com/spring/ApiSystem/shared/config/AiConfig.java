package com.spring.ApiSystem.shared.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean("ollamaClient")
    ChatClient ollamaClient(OllamaChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean("geminiClient")
    ChatClient geminiClient(GoogleGenAiChatModel model) {
        return ChatClient.builder(model).build();
    }
}
