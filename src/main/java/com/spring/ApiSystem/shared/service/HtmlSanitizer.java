package com.spring.ApiSystem.shared.service;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Service
public class HtmlSanitizer {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            .toFactory();

    public String sanitize(String dirtyText) {
        if (dirtyText == null || dirtyText.isBlank()) {
            return dirtyText;
        }
        return POLICY.sanitize(dirtyText);
    }

    public String sanitizeNullable(String dirtyText) {
        if (dirtyText == null) {
            return null;
        }
        if (dirtyText.isBlank()) {
            return dirtyText;
        }
        return POLICY.sanitize(dirtyText);
    }
}
