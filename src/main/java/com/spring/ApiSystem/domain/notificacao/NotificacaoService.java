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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NotificacaoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificationWebSocketService notificationWebSocketService;

    public NotificacaoService(
            NotificacaoRepository notificacaoRepository,
            UsuarioRepository usuarioRepository,
            NotificationWebSocketService notificationWebSocketService
    ) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificationWebSocketService = notificationWebSocketService;
    }

    /**
     * Persiste a notificação no banco de dados e subsequentemente despacha via WebSocket/Push.
     * Garante que usuários deslogados ou offline tenham a notificação armazenada para recuperação posterior.
     */
    @Transactional
    public Notificacao criarNotificacao(Usuario usuario, String titulo, String mensagem, String tipo, String dadosAdicionais) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário destinatário não pode ser nulo.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título da notificação não pode ser vazio.");
        }
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("Mensagem da notificação não pode ser vazia.");
        }

        Notificacao notificacao = new Notificacao(usuario, titulo, mensagem, tipo, dadosAdicionais);
        Notificacao salva = notificacaoRepository.save(notificacao);
        log.info("Notificação persistida no banco com id={} para usuario_id={} tipo={}", salva.getId(), usuario.getId(), tipo);

        // Despacha em tempo real (STOMP se online, Expo Push se offline)
        try {
            NotificationPayload payload = new NotificationPayload(salva.getId(), titulo, mensagem, tipo, dadosAdicionais, salva.getCreatedAt());
            notificationWebSocketService.sendToUser(usuario.getEmail(), payload);
        } catch (Exception e) {
            log.error("Erro ao despachar notificação em tempo real para email={}: {}", usuario.getEmail(), e.getMessage());
        }

        return salva;
    }

    @Transactional
    public Notificacao criarNotificacao(Usuario usuario, String titulo, String mensagem, String tipo) {
        return criarNotificacao(usuario, titulo, mensagem, tipo, null);
    }

    @Transactional
    public Notificacao criarNotificacao(String email, String titulo, String mensagem, String tipo, String dadosAdicionais) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
        return criarNotificacao(usuario, titulo, mensagem, tipo, dadosAdicionais);
    }

    @Transactional
    public Notificacao criarNotificacao(String email, String titulo, String mensagem, String tipo) {
        return criarNotificacao(email, titulo, mensagem, tipo, null);
    }

    @Transactional(readOnly = true)
    public List<ResNotificacaoDTO> buscarNotificacoes(Usuario usuario) {
        return buscarNotificacoesUsuario(usuario, false);
    }

    @Transactional(readOnly = true)
    public List<ResNotificacaoDTO> buscarNotificacoes(Usuario usuario, Boolean apenasNaoLidas) {
        return buscarNotificacoesUsuario(usuario, apenasNaoLidas);
    }

    @Transactional(readOnly = true)
    public List<ResNotificacaoDTO> buscarNotificacoesUsuario(Usuario usuario, Boolean apenasNaoLidas) {
        List<Notificacao> lista;
        if (Boolean.TRUE.equals(apenasNaoLidas)) {
            lista = notificacaoRepository.findAllByUsuarioAndLidaOrderByCreatedAtDesc(usuario, false);
        } else {
            lista = notificacaoRepository.findAllByUsuarioOrderByCreatedAtDesc(usuario);
        }
        return lista.stream().map(ResNotificacaoDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public Page<ResNotificacaoDTO> buscarNotificacoesPaginadas(Usuario usuario, Boolean apenasNaoLidas, Pageable pageable) {
        Page<Notificacao> pagina;
        if (Boolean.TRUE.equals(apenasNaoLidas)) {
            pagina = notificacaoRepository.findAllByUsuarioAndLidaOrderByCreatedAtDesc(usuario, false, pageable);
        } else {
            pagina = notificacaoRepository.findAllByUsuarioOrderByCreatedAtDesc(usuario, pageable);
        }
        return pagina.map(ResNotificacaoDTO::fromEntity);
    }

    @Transactional
    public ResNotificacaoDTO marcarComoLida(Long notificacaoId, Usuario usuario) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new NotificacaoNaoEncontradaException(notificacaoId));

        if (!notificacao.getUsuario().getId().equals(usuario.getId())) {
            log.warn("Tentativa não autorizada: usuario_id={} tentou marcar notificacao_id={} de usuario_id={}",
                    usuario.getId(), notificacaoId, notificacao.getUsuario().getId());
            throw new NotificacaoNaoPertenceAoUsuarioException();
        }

        notificacao.marcarComoLida();
        Notificacao atualizada = notificacaoRepository.save(notificacao);
        return ResNotificacaoDTO.fromEntity(atualizada);
    }

    @Transactional
    public void marcarTodasComoLidas(Usuario usuario) {
        int atualizadas = notificacaoRepository.marcarTodasComoLidas(usuario, Instant.now());
        log.info("Total de {} notificações marcadas como lidas para usuario_id={}", atualizadas, usuario.getId());
    }

    @Transactional(readOnly = true)
    public ResNotificacaoContagemDTO contarNaoLidas(Usuario usuario) {
        long count = notificacaoRepository.countByUsuarioAndLidaFalse(usuario);
        return new ResNotificacaoContagemDTO(count);
    }
}
