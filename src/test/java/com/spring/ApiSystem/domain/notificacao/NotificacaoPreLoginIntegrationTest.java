package com.spring.ApiSystem.domain.notificacao;

import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoDTO;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioRepository;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração - Persistência Pré-Login e Recuperação Pós-Login")
public class NotificacaoPreLoginIntegrationTest {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacaoController notificacaoController;

    private Usuario usuarioOffline;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        usuarioOffline = new Usuario();
        usuarioOffline.setNome("Aluno Offline");
        usuarioOffline.setEmail("aluno.offline@test.com");
        usuarioOffline.setSenha("hashSeguro123");
        usuarioOffline.setSexo("M");
        usuarioOffline.setAtivo(true);
        usuarioOffline.addRole(Role.ALUNO);
        usuarioOffline = usuarioRepository.save(usuarioOffline);
    }

    @Test
    @DisplayName("Garante persistência de notificações geradas pré-login e sua posterior recuperação pós-login")
    void devePersistirNotificacaoPreLoginERecuperarAposLogin() {
        // ETAPA 1: Usuário está deslogado (sem autenticação no SecurityContextHolder)
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // ETAPA 2: Evento do sistema gera notificação para o usuário offline
        Notificacao notificacaoGeradaPreLogin = notificacaoService.criarNotificacao(
                usuarioOffline,
                "✅ Aula confirmada!",
                "Sua aula com o personal foi aprovada com sucesso.",
                "AGENDAMENTO_APROVADO",
                "{\"recipient\":\"aluno\",\"date\":\"2026-11-10\",\"time\":\"10:00\"}"
        );

        assertNotNull(notificacaoGeradaPreLogin.getId(), "A notificação DEVE ter ID gerado pelo banco");
        assertFalse(notificacaoGeradaPreLogin.isLida(), "Notificação nova deve ter lida=false");

        // ETAPA 3: Confirma que o banco de dados armazena a notificação
        List<Notificacao> notificacoesNoBanco = notificacaoRepository.findAllByUsuarioOrderByCreatedAtDesc(usuarioOffline);
        assertEquals(1, notificacoesNoBanco.size());
        assertEquals("✅ Aula confirmada!", notificacoesNoBanco.get(0).getTitulo());

        // ETAPA 4: Usuário agora faz login (simula autenticação com JWT e SecurityContext)
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                usuarioOffline.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ALUNO"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ETAPA 5: Usuário autenticado recupera notificações via controller (GET /api/notificacoes)
        var response = notificacaoController.listarNotificacoes(false);
        assertEquals(200, response.getStatusCode().value());

        List<ResNotificacaoDTO> listaRecuperada = response.getBody();
        assertNotNull(listaRecuperada);
        assertEquals(1, listaRecuperada.size());

        ResNotificacaoDTO item = listaRecuperada.get(0);
        assertEquals(notificacaoGeradaPreLogin.getId(), item.id());
        assertEquals("✅ Aula confirmada!", item.titulo());
        assertEquals("Sua aula com o personal foi aprovada com sucesso.", item.mensagem());
        assertEquals("AGENDAMENTO_APROVADO", item.tipo());
        assertEquals("{\"recipient\":\"aluno\",\"date\":\"2026-11-10\",\"time\":\"10:00\"}", item.dadosAdicionais());
        assertEquals("{\"recipient\":\"aluno\",\"date\":\"2026-11-10\",\"time\":\"10:00\"}", item.data());
        assertFalse(item.lida());

        // ETAPA 6: Usuário lê a notificação (PATCH /api/notificacoes/{id}/lida)
        var responsePatch = notificacaoController.marcarComoLida(item.id());
        assertEquals(200, responsePatch.getStatusCode().value());
        assertTrue(responsePatch.getBody().lida());
        assertNotNull(responsePatch.getBody().readAt());

        // ETAPA 7: Verifica que a notificação foi persistida como lida no banco
        Notificacao notificacaoAtualizada = notificacaoRepository.findById(item.id()).orElseThrow();
        assertTrue(notificacaoAtualizada.isLida());
        assertNotNull(notificacaoAtualizada.getReadAt());
    }
}
