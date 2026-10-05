package com.spring.ApiSystem.shared.service;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    public PromptTemplate carregarPromptTemplate(String caminho) {
        return new PromptTemplate(new ClassPathResource(caminho));
    }
}
