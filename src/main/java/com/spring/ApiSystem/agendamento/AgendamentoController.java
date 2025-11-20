package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.request.ReqCadastrarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReqReagendarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReqRegistrarAusenciaAgendamentoAluno;
import com.spring.ApiSystem.agendamento.dto.request.ReqRegistrarAusenciaAgendamentoPersonal;
import com.spring.ApiSystem.agendamento.dto.response.ResBuscarSolicitacaoPorPersonal;
import com.spring.ApiSystem.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agendamentos", description = "Operações relacionadas a agendamentos")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;

    public AgendamentoController(AgendamentoService agendamentoService, UsuarioService usuarioService) {
        this.agendamentoService = agendamentoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody ReqCadastrarAgendamentoDTO reqCadastrarAgendamentoDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                agendamentoService.criarAgendamento(reqCadastrarAgendamentoDTO)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> buscarAgendamentosPorUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                agendamentoService.buscarAgendamentosPorUsuario()
        );
    }

    @PutMapping("/reagendar")
    public ResponseEntity<?> reagendar(@RequestBody ReqReagendarAgendamentoDTO reqReagendarAgendamentoDTO) {
        agendamentoService.reagendamento(reqReagendarAgendamentoDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable("id") Long agendamentoId) {
        agendamentoService.aprovarAgendamento(agendamentoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable("id") Long agendamentoId) {
        agendamentoService.cancelarAgendamento(agendamentoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirmar-conclusao")
    public ResponseEntity<?> confirmarConclusao(@PathVariable("id") Long agendamentoId) {
        agendamentoService.confirmarConclusao(agendamentoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ausencia/aluno")
    public ResponseEntity<?> registrarAusenciaAlunoPorPersonal(@RequestBody ReqRegistrarAusenciaAgendamentoAluno req) {
        agendamentoService.registrarAusenciaAlunoPorPersonal(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ausencia/personal")
    public ResponseEntity<?> registrarAusenciaPersonalPorPersonal(@RequestBody ReqRegistrarAusenciaAgendamentoPersonal req) {
        agendamentoService.registrarAusenciaPersonalPorPersonal(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<Page<ResBuscarSolicitacaoPorPersonal>> buscarSolicitacaoPorPersonal(Pageable pageable) {
        Page<ResBuscarSolicitacaoPorPersonal> page = agendamentoService.buscarSolicitacaoPorPersonal(pageable);
        return ResponseEntity.ok(page);
    }
}
