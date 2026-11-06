package com.spring.ApiSystem.domain.notificacao;

import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoContagemDTO;
import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoDTO;
import com.spring.ApiSystem.domain.notificacao.exception.NotificacaoNaoEncontradaException;
import com.spring.ApiSystem.domain.notificacao.exception.NotificacaoNaoPertenceAoUsuarioException;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioRepository;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.shared.infrastructure.websocket.NotificationWebSocketService;
import com.spring.ApiSystem.shared.infrastructure.websocket.dto.NotificationPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - NotificacaoService")
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotificationWebSocketService notificationWebSocketService;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Usuario usuario;
    private Usuario outroUsuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        usuario.setEmail("aluno@csf.com");
        usuario.setNome("Aluno Teste");

        outroUsuario = new Usuario();
        ReflectionTestUtils.setField(outroUsuario, "id", 2L);
        outroUsuario.setEmail("outro@csf.com");
        outroUsuario.setNome("Outro Usuario");
    }

    @Test
    @DisplayName("Deve salvar notificação no banco e despachar para o websocket com ID e dados adicionais")
    void deveCriarNotificacaoEPersistirComSucessoEDespachar() {
        String metadata = "{\"recipient\":\"aluno\",\"agendamentoId\":10}";
        Notificacao notificacaoMock = new Notificacao(usuario, "Aula Aprovada", "Sua aula foi confirmada", "AGENDAMENTO_APROVADO", metadata);
        ReflectionTestUtils.setField(notificacaoMock, "id", 10L);
        ReflectionTestUtils.setField(notificacaoMock, "createdAt", Instant.now());

        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacaoMock);

        Notificacao resultado = notificacaoService.criarNotificacao(usuario, "Aula Aprovada", "Sua aula foi confirmada", "AGENDAMENTO_APROVADO", metadata);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Aula Aprovada", resultado.getTitulo());
        assertEquals(metadata, resultado.getDadosAdicionais());

        ArgumentCaptor<NotificationPayload> payloadCaptor = ArgumentCaptor.forClass(NotificationPayload.class);
        verify(notificationWebSocketService).sendToUser(eq("aluno@csf.com"), payloadCaptor.capture());
        assertEquals(10L, payloadCaptor.getValue().id());
        assertEquals("Aula Aprovada", payloadCaptor.getValue().title());
        assertEquals(metadata, payloadCaptor.getValue().dadosAdicionais());
    }

    @Test
    @DisplayName("Deve criar notificação com método de conveniência sem dados adicionais")
    void deveCriarNotificacaoSemDadosAdicionais() {
        Notificacao notificacaoMock = new Notificacao(usuario, "Titulo", "Mensagem", "TIPO", null);
        ReflectionTestUtils.setField(notificacaoMock, "id", 11L);
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacaoMock);

        Notificacao resultado = notificacaoService.criarNotificacao(usuario, "Titulo", "Mensagem", "TIPO");

        assertNotNull(resultado);
        assertEquals(11L, resultado.getId());
        assertNull(resultado.getDadosAdicionais());
    }

    @Test
    @DisplayName("Deve criar notificação a partir de email com dados adicionais")
    void deveCriarNotificacaoPorEmail() {
        when(usuarioRepository.findByEmail("aluno@csf.com")).thenReturn(Optional.of(usuario));
        Notificacao notificacaoMock = new Notificacao(usuario, "Titulo", "Mensagem", "TIPO", "{\"recipient\":\"aluno\"}");
        ReflectionTestUtils.setField(notificacaoMock, "id", 12L);
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacaoMock);

        Notificacao resultado = notificacaoService.criarNotificacao("aluno@csf.com", "Titulo", "Mensagem", "TIPO", "{\"recipient\":\"aluno\"}");

        assertNotNull(resultado);
        assertEquals(12L, resultado.getId());
    }

    @Test
    @DisplayName("Deve criar notificação a partir de email sem dados adicionais")
    void deveCriarNotificacaoPorEmailSemDadosAdicionais() {
        when(usuarioRepository.findByEmail("aluno@csf.com")).thenReturn(Optional.of(usuario));
        Notificacao notificacaoMock = new Notificacao(usuario, "Titulo", "Mensagem", "TIPO", null);
        ReflectionTestUtils.setField(notificacaoMock, "id", 13L);
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacaoMock);

        Notificacao resultado = notificacaoService.criarNotificacao("aluno@csf.com", "Titulo", "Mensagem", "TIPO");

        assertNotNull(resultado);
        assertEquals(13L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção se usuário, título ou mensagem forem inválidos")
    void deveValidarCamposObrigatoriosAoCriarNotificacao() {
        assertThrows(IllegalArgumentException.class, () ->
                notificacaoService.criarNotificacao((Usuario) null, "Titulo", "Mensagem", "TIPO", null));

        assertThrows(IllegalArgumentException.class, () ->
                notificacaoService.criarNotificacao(usuario, null, "Mensagem", "TIPO", null));

        assertThrows(IllegalArgumentException.class, () ->
                notificacaoService.criarNotificacao(usuario, "   ", "Mensagem", "TIPO", null));

        assertThrows(IllegalArgumentException.class, () ->
                notificacaoService.criarNotificacao(usuario, "Titulo", null, "TIPO", null));

        assertThrows(IllegalArgumentException.class, () ->
                notificacaoService.criarNotificacao(usuario, "Titulo", "  ", "TIPO", null));
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando email não for encontrado ao criar notificação")
    void deveLancarUsuarioNaoEncontradoExceptionQuandoEmailNaoExiste() {
        when(usuarioRepository.findByEmail("inexistente@csf.com")).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            notificacaoService.criarNotificacao("inexistente@csf.com", "Titulo", "Mensagem", "AGENDAMENTO_APROVADO", "{\"recipient\":\"aluno\"}");
        });
        verify(notificacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar notificações de usuário ordenadas")
    void deveBuscarNotificacoesDoUsuarioOrdenadas() {
        Notificacao n1 = new Notificacao(usuario, "T1", "M1", "TYPE1");
        ReflectionTestUtils.setField(n1, "id", 1L);
        ReflectionTestUtils.setField(n1, "createdAt", Instant.now());

        when(notificacaoRepository.findAllByUsuarioOrderByCreatedAtDesc(usuario)).thenReturn(List.of(n1));

        List<ResNotificacaoDTO> lista = notificacaoService.buscarNotificacoesUsuario(usuario, false);

        assertEquals(1, lista.size());
        assertEquals("T1", lista.get(0).titulo());
        verify(notificacaoRepository).findAllByUsuarioOrderByCreatedAtDesc(usuario);
    }

    @Test
    @DisplayName("Deve buscar apenas notificações não lidas quando flag apenasNaoLidas for true")
    void deveBuscarApenasNotificacoesNaoLidas() {
        Notificacao n1 = new Notificacao(usuario, "T1", "M1", "TYPE1");
        ReflectionTestUtils.setField(n1, "id", 1L);
        ReflectionTestUtils.setField(n1, "createdAt", Instant.now());

        when(notificacaoRepository.findAllByUsuarioAndLidaOrderByCreatedAtDesc(usuario, false)).thenReturn(List.of(n1));

        List<ResNotificacaoDTO> lista = notificacaoService.buscarNotificacoesUsuario(usuario, true);

        assertEquals(1, lista.size());
        assertEquals("T1", lista.get(0).titulo());
        verify(notificacaoRepository).findAllByUsuarioAndLidaOrderByCreatedAtDesc(usuario, false);
    }

    @Test
    @DisplayName("Deve buscar notificações paginadas com e sem filtro de não lidas")
    void deveBuscarNotificacoesPaginadas() {
        Notificacao n1 = new Notificacao(usuario, "T1", "M1", "TYPE1");
        ReflectionTestUtils.setField(n1, "id", 1L);
        ReflectionTestUtils.setField(n1, "createdAt", Instant.now());
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(notificacaoRepository.findAllByUsuarioOrderByCreatedAtDesc(usuario, pageRequest))
                .thenReturn(new PageImpl<>(List.of(n1), pageRequest, 1));
        Page<ResNotificacaoDTO> pag1 = notificacaoService.buscarNotificacoesPaginadas(usuario, false, pageRequest);
        assertEquals(1, pag1.getTotalElements());

        when(notificacaoRepository.findAllByUsuarioAndLidaOrderByCreatedAtDesc(usuario, false, pageRequest))
                .thenReturn(new PageImpl<>(List.of(n1), pageRequest, 1));
        Page<ResNotificacaoDTO> pag2 = notificacaoService.buscarNotificacoesPaginadas(usuario, true, pageRequest);
        assertEquals(1, pag2.getTotalElements());
    }

    @Test
    @DisplayName("Deve marcar notificação como lida quando pertence ao usuário")
    void deveMarcarNotificacaoComoLidaQuandoPertenceAoUsuario() {
        Notificacao n = new Notificacao(usuario, "T1", "M1", "TYPE1");
        ReflectionTestUtils.setField(n, "id", 100L);
        assertFalse(n.isLida());

        when(notificacaoRepository.findById(100L)).thenReturn(Optional.of(n));
        when(notificacaoRepository.save(any(Notificacao.class))).thenAnswer(inv -> inv.getArgument(0));

        ResNotificacaoDTO res = notificacaoService.marcarComoLida(100L, usuario);

        assertTrue(res.lida());
        assertNotNull(res.readAt());
    }

    @Test
    @DisplayName("Deve lançar 403 Forbidden quando usuário tenta marcar notificação de outro usuário")
    void deveLancarExcecaoQuandoNotificacaoNaoPertenceAoUsuario() {
        Notificacao n = new Notificacao(outroUsuario, "T1", "M1", "TYPE1");
        ReflectionTestUtils.setField(n, "id", 100L);

        when(notificacaoRepository.findById(100L)).thenReturn(Optional.of(n));

        assertThrows(NotificacaoNaoPertenceAoUsuarioException.class, () -> {
            notificacaoService.marcarComoLida(100L, usuario);
        });
        verify(notificacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar 404 Not Found quando notificação não existe")
    void deveLancarExcecaoQuandoNotificacaoNaoEncontrada() {
        when(notificacaoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotificacaoNaoEncontradaException.class, () -> {
            notificacaoService.marcarComoLida(999L, usuario);
        });
    }

    @Test
    @DisplayName("Deve marcar todas as notificações como lidas")
    void deveMarcarTodasComoLidas() {
        when(notificacaoRepository.marcarTodasComoLidas(eq(usuario), any(Instant.class))).thenReturn(5);

        notificacaoService.marcarTodasComoLidas(usuario);

        verify(notificacaoRepository).marcarTodasComoLidas(eq(usuario), any(Instant.class));
    }

    @Test
    @DisplayName("Deve retornar contagem de notificações não lidas")
    void deveRetornarContagemDeNaoLidas() {
        when(notificacaoRepository.countByUsuarioAndLidaFalse(usuario)).thenReturn(3L);

        ResNotificacaoContagemDTO contagem = notificacaoService.contarNaoLidas(usuario);

        assertEquals(3L, contagem.unreadCount());
    }
}
