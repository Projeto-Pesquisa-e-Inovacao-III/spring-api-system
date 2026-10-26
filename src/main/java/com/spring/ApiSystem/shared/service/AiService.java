package com.spring.ApiSystem.shared.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient ollama;
    private final ChatClient gemini;

    public AiService(
            @Qualifier("ollamaClient") ChatClient ollama,
            @Qualifier("geminiClient") ChatClient gemini) {
        this.ollama = ollama;
        this.gemini = gemini;
    }

    @Value("${app.ai.provider:ollama}") String provider;

    public String usarLLM(PromptTemplate promptSystem, Prompt inputs){
        if(provider.equals("ollama")) {
            return usarOllama(promptSystem, inputs);
        } else if (provider.equals("gemini")) {
            return usarGemini(promptSystem, inputs);
        }

        throw new IllegalArgumentException("Provedor de IA inválido");
    }

    public String usarOllama(PromptTemplate promptSystem, Prompt inputs) {
        return ollama.prompt()
                .system(promptSystem.getTemplate())
                .user(inputs.getContents())
                .options(OllamaChatOptions.builder()
                        .format("json")
                        .numCtx(32768)
                        .disableThinking())
                .call()
                .content();
    }

    public String usarGemini(PromptTemplate promptSystem, Prompt inputs) {
        return gemini.prompt()
                .system(promptSystem.getTemplate())
                .user(inputs.getContents())
                .options(GoogleGenAiChatOptions.builder()
                        .responseMimeType("application/json"))
                .call()
                .content();
    }

    public PromptTemplate carregarPromptTemplate(String caminho) {
        return new PromptTemplate(new ClassPathResource(caminho));
    }
}
