
package com.spring.ApiSystem.domain.agendamento;

import com.spring.ApiSystem.domain.agendamento.dto.request.*;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResListarConsultoriasRealizadasDto;
import com.spring.ApiSystem.shared.service.PageableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Agendamentos", description = "Operações relacionadas a agendamentos")
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private static final int PAGE_SIZE_FILTRAR = 8;
    private static final int PAGE_SIZE_SOLICITACOES = 30;

    private final AgendamentoService agendamentoService;
    private final PageableService pageableService;

    public AgendamentoController(AgendamentoService agendamentoService, PageableService pageableService) {
        this.agendamentoService = agendamentoService;
        this.pageableService = pageableService;
    }

    /* -------------------- Criação e ações sobre agendamentos -------------------- */

    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento e retorna o recurso criado com status 201.")
    @PostMapping
    public ResponseEntity<?> criarAgendamento(@Valid @RequestBody ReqCadastrarAgendamentoDTO reqCadastrarAgendamentoDTO) {
        Object criado = agendamentoService.criarAgendamento(reqCadastrarAgendamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Reagendar agendamento", description = "Reagenda um agendamento existente. Retorna 204 quando bem sucedido.")
    @PutMapping("/reagendar")
    public ResponseEntity<?> reagendar(@Valid @RequestBody ReqReagendarAgendamentoDTO reqReagendarAgendamentoDTO) {
        agendamentoService.reagendamento(reqReagendarAgendamentoDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Aprovar agendamento", description = "Aprova um agendamento pelo ID. Retorna 204 quando bem sucedido.")
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable("id") Long agendamentoId) {
        agendamentoService.aprovarAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento pelo ID. Retorna 204 quando bem sucedido.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable("id") Long agendamentoId) {
        agendamentoService.cancelarAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Confirmar conclusão", description = "Confirma a conclusão de um agendamento pelo ID. Retorna 204 quando bem sucedido.")
    @PutMapping("/{id}/confirmar-conclusao")
    public ResponseEntity<?> confirmarConclusao(@PathVariable("id") Long agendamentoId) {
        agendamentoService.confirmarConclusao(agendamentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Registrar ausência", description = "Registra ausência para um agendamento. Retorna 204 quando bem sucedido.")
    @PutMapping("/ausencia")
    public ResponseEntity<?> registrarAusencia(@Valid @RequestBody ReqRegistrarAusenciaAgendamento req) {
        agendamentoService.registrarAusencia(req);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /* -------------------- Consultas e listagens -------------------- */

    @Operation(summary = "Buscar solicitações por personal", description = "Retorna solicitações paginadas por tipo de usuário.")
    @GetMapping("/solicitacoes")
    public ResponseEntity<Page<?>> buscarSolicitacaoPorPersonal(@ModelAttribute ReqGetAgendamentoDto dto,
                                                                Pageable pageable) {
        Pageable pageableWithSetSize= pageableService.setMaxSizePageable(pageable, PAGE_SIZE_SOLICITACOES);
        Page<?> page = agendamentoService.buscarSolicitacaoPorTipoUsuarios(dto, pageableWithSetSize);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Buscar agendamentos para calendário", description = "Retorna lista de agendamentos formatados para exibição em calendário.")
    @GetMapping("/calendario")
    public ResponseEntity<List<?>> buscarAgendamentosParaCalendario() {
        return ResponseEntity.ok(agendamentoService.buscarAgendamentosParaCalendario());
    }

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os dados de um agendamento pelo seu ID.")
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> buscarDadosDoAgendamentoPorId(@PathVariable("id") Long agendamentoId) {
        Object dados = agendamentoService.buscarDadosDoAgendamentoPorId(agendamentoId);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar agendamentos do usuário", description = "Retorna os agendamentos do usuário autenticado.")
    @GetMapping("/me")
    public ResponseEntity<?> buscarAgendamentosPorUsuario() {
        return ResponseEntity.ok(agendamentoService.buscarAgendamentosPorUsuario());
    }

    @Operation(summary = "Contar agendamentos por personal, status e data",
            description = "Retorna a contagem de agendamentos de um personal, filtrados por status e data específica.")
    @PostMapping("/contagem-status-data")
    public ResponseEntity<Integer> contarPorPersonalStatusEData(@Valid @RequestBody ReqContarAgendamentoPorPersonalStatusDataDto dto) {
        Integer contagem = agendamentoService.buscarContagemDeAgendamentosPorPersonalStatusData(dto.status(), dto.data());
        return ResponseEntity.ok(contagem);
    }

    @Operation(summary = "Conta as consultorias realizadas por mês do personal logado",
            description = "Retorna a quantidade de consultorias realizadas nos últimos meses do personal logado.")
    @GetMapping("/consultoria-realizadas/{quantidadeMeses}")
    public ResponseEntity<List<ResListarConsultoriasRealizadasDto>> listarConsultoriasRealizadasMeses(@PathVariable Integer quantidadeMeses) {
        return ResponseEntity.ok(agendamentoService.listarConsultoriasRealizadasMes(quantidadeMeses));
    }
}
