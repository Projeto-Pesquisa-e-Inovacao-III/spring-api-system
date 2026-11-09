package com.spring.ApiSystem.shared.infrastructure.telemetry;

import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - TelemetryMdcFilter")
class TelemetryMdcFilterTest {

    @Mock
    private JpaUserDetailsService userDetailsService;

    private TelemetryMdcFilter filter;

    @BeforeEach
    void setUp() {
        filter = new TelemetryMdcFilter(userDetailsService);
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("Deve extrair headers válidos, popular MDC com ID do banco (LGPD) e limpar após resposta")
    void deveExtrairHeadersValidosPopularELimparMdc() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/produtos-exibicoes/ativos");
        request.addHeader("X-OS", "Android");
        request.addHeader("X-OS-Version", "14.0");
        request.addHeader("X-App-Version", "1.2.3");
        request.addHeader("X-App-Build", "42");
        request.addHeader("X-Request-Id", "req-12345");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Usuario usuario = new Usuario();
        usuario.setId(42L);
        usuario.setEmail("aluno@csf.com");
        when(userDetailsService.isLogged()).thenReturn(Optional.of(usuario));

        AtomicReference<String> capturedOs = new AtomicReference<>();
        AtomicReference<String> capturedOsVersion = new AtomicReference<>();
        AtomicReference<String> capturedAppVersion = new AtomicReference<>();
        AtomicReference<String> capturedAppBuild = new AtomicReference<>();
        AtomicReference<String> capturedRequestId = new AtomicReference<>();

        FilterChain chain = (req, res) -> {
            capturedOs.set(MDC.get(TelemetryMdcFilter.MDC_OS));
            capturedOsVersion.set(MDC.get(TelemetryMdcFilter.MDC_OS_VERSION));
            capturedAppVersion.set(MDC.get(TelemetryMdcFilter.MDC_APP_VERSION));
            capturedAppBuild.set(MDC.get(TelemetryMdcFilter.MDC_APP_BUILD));
            capturedRequestId.set(MDC.get(TelemetryMdcFilter.MDC_REQUEST_ID));
        };

        filter.doFilter(request, response, chain);

        assertEquals("android", capturedOs.get());
        assertEquals("14.0", capturedOsVersion.get());
        assertEquals("1.2.3", capturedAppVersion.get());
        assertEquals("42", capturedAppBuild.get());
        assertEquals("req-12345", capturedRequestId.get());
        assertEquals("42", filter.resolveCachedUserId());
        assertEquals("req-12345", response.getHeader("X-Request-Id"));

        // Verifica limpeza do MDC no bloco finally
        assertNull(MDC.get(TelemetryMdcFilter.MDC_OS));
        assertNull(MDC.get(TelemetryMdcFilter.MDC_OS_VERSION));
        assertNull(MDC.get(TelemetryMdcFilter.MDC_APP_VERSION));
        assertNull(MDC.get(TelemetryMdcFilter.MDC_APP_BUILD));
        assertNull(MDC.get(TelemetryMdcFilter.MDC_REQUEST_ID));
        assertNull(MDC.get(TelemetryMdcFilter.MDC_USER_ID));
    }

    @Test
    @DisplayName("Deve sanitizar headers maliciosos ou inválidos para 'unknown' e gerar UUID de requestId")
    void deveSanitizarHeadersMaliciososOuAusentes() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/usuarios/login");
        request.addHeader("X-OS", "windows-xp; DROP TABLE usuario;");
        request.addHeader("X-OS-Version", "14\nmalicious_injection");
        request.addHeader("X-App-Version", "a".repeat(40));
        request.addHeader("X-App-Build", "<script>alert(1)</script>");
        request.addHeader("X-Request-Id", "bad request id with spaces!");

        MockHttpServletResponse response = new MockHttpServletResponse();
        when(userDetailsService.isLogged()).thenReturn(Optional.empty());

        AtomicReference<String> capturedOs = new AtomicReference<>();
        AtomicReference<String> capturedOsVersion = new AtomicReference<>();
        AtomicReference<String> capturedAppVersion = new AtomicReference<>();
        AtomicReference<String> capturedAppBuild = new AtomicReference<>();
        AtomicReference<String> capturedRequestId = new AtomicReference<>();

        FilterChain chain = (req, res) -> {
            capturedOs.set(MDC.get(TelemetryMdcFilter.MDC_OS));
            capturedOsVersion.set(MDC.get(TelemetryMdcFilter.MDC_OS_VERSION));
            capturedAppVersion.set(MDC.get(TelemetryMdcFilter.MDC_APP_VERSION));
            capturedAppBuild.set(MDC.get(TelemetryMdcFilter.MDC_APP_BUILD));
            capturedRequestId.set(MDC.get(TelemetryMdcFilter.MDC_REQUEST_ID));
        };

        filter.doFilter(request, response, chain);

        assertEquals("unknown", capturedOs.get());
        assertEquals("unknown", capturedOsVersion.get());
        assertEquals("unknown", capturedAppVersion.get());
        assertEquals("unknown", capturedAppBuild.get());
        assertNotNull(capturedRequestId.get());
        assertEquals(36, capturedRequestId.get().length());

        // Verifica limpeza do MDC após requisição
        assertNull(MDC.get(TelemetryMdcFilter.MDC_OS));
        assertNull(MDC.get(TelemetryMdcFilter.MDC_REQUEST_ID));
    }

    @Test
    @DisplayName("Deve ignorar requisições fora de /api/ (ex: /actuator/prometheus)")
    void deveIgnorarRequisicoesActuatorPrometheus() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/prometheus");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);

        FilterChain chain = (req, res) -> {
            chainCalled.set(true);
            assertNull(MDC.get(TelemetryMdcFilter.MDC_REQUEST_ID));
        };

        filter.doFilter(request, response, chain);

        assertTrue(chainCalled.get());
        assertNull(response.getHeader("X-Request-Id"));
    }
}
