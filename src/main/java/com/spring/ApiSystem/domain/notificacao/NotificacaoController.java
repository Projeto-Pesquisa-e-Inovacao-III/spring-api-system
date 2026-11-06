package com.spring.ApiSystem.domain.notificacao;

import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoContagemDTO;
import com.spring.ApiSystem.domain.notificacao.dto.response.ResNotificacaoDTO;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notificações", description = "Operações relacionadas a notificações do usuário")
@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final JpaUserDetailsService userDetailsService;

    public NotificacaoController(
            NotificacaoService notificacaoService,
            JpaUserDetailsService userDetailsService
    ) {
        this.notificacaoService = notificacaoService;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Listar notificações", description = "Retorna o histórico de notificações do usuário autenticado")
    @GetMapping
    public ResponseEntity<List<ResNotificacaoDTO>> listarNotificacoes(
            @RequestParam(name = "apenasNaoLidas", required = false, defaultValue = "false") Boolean apenasNaoLidas
    ) {
        Usuario usuario = userDetailsService.getCurrentUser();
        List<ResNotificacaoDTO> resultado = notificacaoService.buscarNotificacoesUsuario(usuario, apenasNaoLidas);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Listar notificações paginadas", description = "Retorna notificações com paginação")
    @GetMapping("/paginadas")
    public ResponseEntity<Page<ResNotificacaoDTO>> listarNotificacoesPaginadas(
            @RequestParam(name = "apenasNaoLidas", required = false, defaultValue = "false") Boolean apenasNaoLidas,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size
    ) {
        Usuario usuario = userDetailsService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<ResNotificacaoDTO> resultado = notificacaoService.buscarNotificacoesPaginadas(usuario, apenasNaoLidas, pageable);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Marcar notificação como lida", description = "Atualiza o status de uma notificação para lida")
    @PatchMapping("/{id}/lida")
    public ResponseEntity<ResNotificacaoDTO> marcarComoLida(@PathVariable Long id) {
        Usuario usuario = userDetailsService.getCurrentUser();
        ResNotificacaoDTO atualizada = notificacaoService.marcarComoLida(id, usuario);
        return ResponseEntity.ok(atualizada);
    }

    @Operation(summary = "Marcar todas como lidas", description = "Atualiza todas as notificações não lidas do usuário para lidas")
    @PatchMapping(value = {"/todas-lidas", "/marcar-todas-lidas"})
    public ResponseEntity<Void> marcarTodasComoLidas() {
        Usuario usuario = userDetailsService.getCurrentUser();
        notificacaoService.marcarTodasComoLidas(usuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar notificações não lidas", description = "Retorna a quantidade de notificações não lidas do usuário")
    @GetMapping("/contagem-nao-lidas")
    public ResponseEntity<ResNotificacaoContagemDTO> contarNaoLidas() {
        Usuario usuario = userDetailsService.getCurrentUser();
        ResNotificacaoContagemDTO contagem = notificacaoService.contarNaoLidas(usuario);
        return ResponseEntity.ok(contagem);
    }
}
