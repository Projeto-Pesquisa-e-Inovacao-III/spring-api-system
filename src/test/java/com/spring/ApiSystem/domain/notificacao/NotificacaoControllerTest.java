package com.spring.ApiSystem.domain.notificacao;

import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoContagemDTO;
import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoDTO;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - NotificacaoController")
class NotificacaoControllerTest {

    @Mock
    private NotificacaoService notificacaoService;

    @Mock
    private JpaUserDetailsService userDetailsService;

    @InjectMocks
    private NotificacaoController notificacaoController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        usuario.setEmail("usuario@csf.com");
    }

    @Test
    @DisplayName("GET /api/notificacoes - Deve retornar 200 com lista de notificações")
    void deveListarNotificacoesComSucesso() {
        when(userDetailsService.getCurrentUser()).thenReturn(usuario);
        ResNotificacaoDTO dto = new ResNotificacaoDTO(1L, "Titulo", "Mensagem", "TIPO", null, false, Instant.now(), null);
        when(notificacaoService.buscarNotificacoesUsuario(usuario, false)).thenReturn(List.of(dto));

        ResponseEntity<List<ResNotificacaoDTO>> response = notificacaoController.listarNotificacoes(false);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Titulo", response.getBody().get(0).titulo());
    }

    @Test
    @DisplayName("GET /api/notificacoes/paginadas - Deve retornar 200 com página de notificações")
    void deveListarNotificacoesPaginadasComSucesso() {
        when(userDetailsService.getCurrentUser()).thenReturn(usuario);
        ResNotificacaoDTO dto = new ResNotificacaoDTO(1L, "Titulo", "Mensagem", "TIPO", null, false, Instant.now(), null);
        Pageable pageable = PageRequest.of(0, 50);
        when(notificacaoService.buscarNotificacoesPaginadas(usuario, false, pageable))
                .thenReturn(new PageImpl<>(List.of(dto), pageable, 1));

        ResponseEntity<Page<ResNotificacaoDTO>> response = notificacaoController.listarNotificacoesPaginadas(false, 0, 50);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("PATCH /api/notificacoes/{id}/lida - Deve retornar 200 com notificação atualizada")
    void deveMarcarComoLidaComSucesso() {
        when(userDetailsService.getCurrentUser()).thenReturn(usuario);
        ResNotificacaoDTO dto = new ResNotificacaoDTO(1L, "Titulo", "Mensagem", "TIPO", null, true, Instant.now(), Instant.now());
        when(notificacaoService.marcarComoLida(1L, usuario)).thenReturn(dto);

        ResponseEntity<ResNotificacaoDTO> response = notificacaoController.marcarComoLida(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody().lida());
    }

    @Test
    @DisplayName("PATCH /api/notificacoes/todas-lidas - Deve retornar 204 No Content")
    void deveMarcarTodasComoLidasComSucesso() {
        when(userDetailsService.getCurrentUser()).thenReturn(usuario);

        ResponseEntity<Void> response = notificacaoController.marcarTodasComoLidas();

        assertEquals(204, response.getStatusCode().value());
        verify(notificacaoService).marcarTodasComoLidas(usuario);
    }

    @Test
    @DisplayName("GET /api/notificacoes/contagem-nao-lidas - Deve retornar 200 com contagem")
    void deveRetornarContagemDeNaoLidasComSucesso() {
        when(userDetailsService.getCurrentUser()).thenReturn(usuario);
        when(notificacaoService.contarNaoLidas(usuario)).thenReturn(new ResNotificacaoContagemDTO(4L));

        ResponseEntity<ResNotificacaoContagemDTO> response = notificacaoController.contarNaoLidas();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(4L, response.getBody().unreadCount());
    }
}
