package com.spring.ApiSystem.shared.infrastructure.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários e MockMvc - MobileLogIngestionController")
class MobileLogIngestionControllerTest {

    @Mock
    private JpaUserDetailsService userDetailsService;

    private MobileLogRateLimiter rateLimiter;
    private MobileLogIngestionService ingestionService;
    private MobileLogIngestionController controller;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        rateLimiter = new MobileLogRateLimiter();
        ingestionService = new MobileLogIngestionService(rateLimiter, userDetailsService);
        controller = new MobileLogIngestionController(ingestionService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilters(new TelemetryMdcFilter(userDetailsService))
                .build();
    }

    @Test
    @DisplayName("POST /api/logs/mobile - Deve aceitar lote válido sem autenticação (anônimo) com status 202")
    void deveAceitarLoteValidoAnonimoComStatus202() throws Exception {
        when(userDetailsService.isLogged()).thenReturn(Optional.empty());

        MobileLogBatchDTO batch = new MobileLogBatchDTO(
                "2026-11-06T19:30:00.000Z",
                "android",
                "14",
                "1.0.0",
                "1",
                List.of(
                        new MobileLogEntryDTO(
                                "ERROR",
                                "UNHANDLED_EXCEPTION",
                                "TypeError: Cannot read property 'id' of undefined",
                                "TypeError: Cannot read property...\n  at UserProfile (UserProfile.tsx:42)",
                                "2026-11-06T19:29:55.120Z",
                                Map.of("screen", "/(app)/(tabs)/dashboard", "action", "load_user_profile")
                        ),
                        new MobileLogEntryDTO(
                                "WARN",
                                "API_ERROR",
                                "HTTP 500 on GET /api/agendamentos/usuario",
                                null,
                                "2026-11-06T19:29:58.200Z",
                                Map.of(
                                        "url", "/api/agendamentos/usuario",
                                        "method", "GET",
                                        "status", 500,
                                        "durationMs", 342,
                                        "requestId", "c1f7b029-7928-4e8c-8f15-3e284b7d1e89"
                                )
                        )
                )
        );

        mockMvc.perform(post("/api/logs/mobile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-OS", "android")
                        .header("X-OS-Version", "14")
                        .header("X-App-Version", "1.0.0")
                        .header("X-App-Build", "1")
                        .content(objectMapper.writeValueAsString(batch)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("accepted"))
                .andExpect(jsonPath("$.ingested").value(2));
    }

    @Test
    @DisplayName("POST /api/logs/mobile - Deve aceitar lote quando usuário está autenticado (usando ID do banco)")
    void deveAceitarLoteComUsuarioAutenticado() {
        Usuario usuario = new Usuario();
        usuario.setId(42L);
        usuario.setEmail("aluno@csf.com");
        when(userDetailsService.isLogged()).thenReturn(Optional.of(usuario));

        MobileLogBatchDTO batch = new MobileLogBatchDTO(
                "2026-11-06T19:30:00.000Z",
                "ios",
                "17.4",
                "1.0.0",
                "1",
                List.of(
                        new MobileLogEntryDTO(
                                "ERROR",
                                "API_ERROR",
                                "Timeout on POST /api/agendamentos",
                                null,
                                "2026-11-06T19:30:00.000Z",
                                Map.of("url", "/api/agendamentos", "method", "POST")
                        )
                )
        );

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/logs/mobile");
        request.setRemoteAddr("10.0.0.15");

        var response = controller.ingestLogs(batch, request);
        assertEquals(202, response.getStatusCode().value());
        assertEquals("accepted", response.getBody().get("status"));
        assertEquals(1, response.getBody().get("ingested"));
    }

    @Test
    @DisplayName("POST /api/logs/mobile - Deve retornar 429 Too Many Requests via GlobalExceptionHandler após exceder 60 req/min por IP")
    void deveRetornar429AposExcederRateLimitPorIp() throws Exception {
        when(userDetailsService.isLogged()).thenReturn(Optional.empty());

        MobileLogBatchDTO batch = new MobileLogBatchDTO(
                "2026-11-06T19:30:00.000Z",
                "android",
                "14",
                "1.0.0",
                "1",
                List.of(
                        new MobileLogEntryDTO(
                                "ERROR",
                                "UNHANDLED_EXCEPTION",
                                "Crash test",
                                null,
                                "2026-11-06T19:30:00.000Z",
                                Map.of()
                        )
                )
        );
        String payload = objectMapper.writeValueAsString(batch);

        for (int i = 0; i < 60; i++) {
            mockMvc.perform(post("/api/logs/mobile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Forwarded-For", "192.168.1.50")
                            .content(payload))
                    .andExpect(status().isAccepted());
        }

        // 61ª requisição do mesmo IP deve ser bloqueada com 429 via CustomApiException -> GlobalExceptionHandler
        mockMvc.perform(post("/api/logs/mobile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Forwarded-For", "192.168.1.50")
                        .content(payload))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429));

        // Outro IP deve continuar sendo aceito normalmente
        mockMvc.perform(post("/api/logs/mobile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Forwarded-For", "192.168.1.51")
                        .content(payload))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("POST /api/logs/mobile - Deve rejeitar lote com mais de 50 entradas com status 400 via GlobalExceptionHandler")
    void deveRejeitarLoteAcimaDoLimiteMaximoDeEntradas() throws Exception {
        when(userDetailsService.isLogged()).thenReturn(Optional.empty());

        List<MobileLogEntryDTO> oversizedEntries = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            oversizedEntries.add(new MobileLogEntryDTO(
                    "WARN",
                    "API_ERROR",
                    "Error " + i,
                    null,
                    "2026-11-06T19:30:00.000Z",
                    Map.of()
            ));
        }

        MobileLogBatchDTO batch = new MobileLogBatchDTO(
                "2026-11-06T19:30:00.000Z",
                "android",
                "14",
                "1.0.0",
                "1",
                oversizedEntries
        );

        mockMvc.perform(post("/api/logs/mobile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batch)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
