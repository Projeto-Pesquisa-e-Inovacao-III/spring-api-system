package com.spring.ApiSystem.shared.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlSanitizerTest {

    private final HtmlSanitizer htmlSanitizer = new HtmlSanitizer();

    @Test
    void deveRemoverTodasAsTagsInclusiveContainer() {
        String dirty = "<img src=\"x\" onerror=\"alert('XSS')\"> <div onclick=\"alert('XSS')\">click</div>";

        String sanitized = htmlSanitizer.sanitize(dirty);

        assertThat(sanitized).isEqualTo(" click");
    }

    @Test
    void deveManterNuloOuBrancoSemAlteracao() {
        assertThat(htmlSanitizer.sanitize(null)).isNull();
        assertThat(htmlSanitizer.sanitize("   ")).isEqualTo("   ");
    }
}

