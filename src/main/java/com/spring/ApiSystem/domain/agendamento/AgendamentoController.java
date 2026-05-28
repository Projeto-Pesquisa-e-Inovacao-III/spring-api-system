package com.spring.ApiSystem.domain.agendamento;

import com.spring.ApiSystem.domain.agendamento.dto.request.*;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResAgendamentoByDayOfWeekDto;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResListarConsultoriasRealizadasDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.calendario.AgendamentoCalendarioResponse;
import com.spring.ApiSystem.domain.agendamento.dto.response.detalhes.AgendamentoDetalheResponse;
import com.spring.ApiSystem.domain.agendamento.dto.response.overview.AgendamentoOverviewResponse;
import com.spring.ApiSystem.domain.agendamento.dto.response.solicitacao.AgendamentoSolicitacaoResponse;
import com.spring.ApiSystem.domain.recomendacaotreino.RecomendacaoTreinoService;
import com.spring.ApiSystem.domain.resumoagendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResResumoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.projection.ResAgendamentoWithResumeProjection;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import com.spring.ApiSystem.shared.service.PageableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
    private final RecomendacaoTreinoService recomendacaoTreinoService;
    private final PageableService pageableService;

    public AgendamentoController(AgendamentoService agendamentoService,
                                 RecomendacaoTreinoService recomendacaoTreinoService,
                                 PageableService pageableService) {
        this.agendamentoService = agendamentoService;
        this.recomendacaoTreinoService = recomendacaoTreinoService;
        this.pageableService = pageableService;
    }

    /* -------------------- IA -------------------- */
    @GetMapping("{agendamentoId}/recomendacao-treino")
    public ResponseEntity<?> gerarRecomendacaoTreino(@PathVariable Long agendamentoId){
        return ResponseEntity.ok(recomendacaoTreinoService.gerarRecomendacaoTreino(agendamentoId));
    }

//    @GetMapping("/{agendamentoId}/recomendacao-treino")
//    public ResponseEntity<?> consultarRecomendacaoTreino(){
//        return null;
//    }

    /* -------------------- Criação e ações sobre agendamentos -------------------- */

    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento e retorna o recurso criado com status 201.")
    @PostMapping
    public ResponseEntity<ResCriarAgendamentoDTO> criarAgendamento(
            @Valid @RequestBody ReqCadastrarAgendamentoDTO reqCadastrarAgendamentoDTO
    ) {
        ResCriarAgendamentoDTO criado = agendamentoService.criarAgendamento(reqCadastrarAgendamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Reagendar agendamento", description = "Reagenda um agendamento existente. Retorna 204 quando bem sucedido.")
    @PutMapping("/reagendar")
    public ResponseEntity<Void> reagendar(
            @Valid @RequestBody ReqReagendarAgendamentoDTO reqReagendarAgendamentoDTO
    ) {
        agendamentoService.reagendamento(reqReagendarAgendamentoDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Aprovar agendamento", description = "Aprova um agendamento pelo ID. Retorna 204 quando bem sucedido.")
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable("id") Long agendamentoId) {
        agendamentoService.aprovarAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento pelo ID. Retorna 204 quando bem sucedido.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable("id") Long agendamentoId) {
        agendamentoService.cancelarAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Confirmar conclusão", description = "Confirma a conclusão de um agendamento pelo ID. Retorna 204 quando bem sucedido.")
    @PutMapping("/{id}/confirmar-conclusao")
    public ResponseEntity<Void> confirmarConclusao(@PathVariable("id") Long agendamentoId,
                                                   @Valid @RequestBody ReqCadastrarResumoAgendamentoDTO dto) {
        agendamentoService.confirmarConclusao(agendamentoId, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Registrar ausência", description = "Registra ausência para um agendamento. Retorna 204 quando bem sucedido.")
    @PutMapping("/ausencia")
    public ResponseEntity<Void> registrarAusencia(
            @Valid @RequestBody ReqRegistrarAusenciaAgendamento req
    ) {
        agendamentoService.registrarAusencia(req);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /* -------------------- Consultas e listagens -------------------- */

    @Operation(summary = "Buscar solicitações por personal", description = "Retorna solicitações paginadas por tipo de usuário.")
    @GetMapping("/solicitacoes")
    public ResponseEntity<Page<AgendamentoSolicitacaoResponse>> buscarSolicitacaoPorPersonal(
            @ModelAttribute ReqGetAgendamentoDto dto,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "data", direction = Sort.Direction.DESC),
            }) Pageable pageable
    ) {
        Pageable pageableWithSetSize = pageableService.setMaxSizePageable(pageable, PAGE_SIZE_SOLICITACOES);
        Page<AgendamentoSolicitacaoResponse> page =
                agendamentoService.buscarSolicitacaoPorTipoUsuarios(dto, pageableWithSetSize);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Buscar agendamentos para calendário", description = "Retorna lista de agendamentos formatados para exibição em calendário.")
    @GetMapping("/calendario")
    public ResponseEntity<List<AgendamentoCalendarioResponse>> buscarAgendamentosParaCalendario() {
        return ResponseEntity.ok(agendamentoService.buscarAgendamentosParaCalendario());
    }

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os dados de um agendamento pelo seu ID.")
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<AgendamentoDetalheResponse> buscarDadosDoAgendamentoPorId(
            @PathVariable("id") Long agendamentoId
    ) {
        AgendamentoDetalheResponse dados = agendamentoService.buscarDadosDoAgendamentoPorId(agendamentoId);
        return ResponseEntity.ok(dados);
    }

    @Operation(summary = "Buscar agendamentos do usuário", description = "Retorna os agendamentos do usuário autenticado.")
    @GetMapping("/me")
    public ResponseEntity<List<AgendamentoOverviewResponse>> buscarAgendamentosPorUsuario() {
        return ResponseEntity.ok(agendamentoService.buscarAgendamentosPorUsuario());
    }

    @Operation(
            summary = "Contar agendamentos por personal, status e data",
            description = "Retorna a contagem de agendamentos de um personal, filtrados por status e data específica."
    )
    @PostMapping("/contagem-status-data")
    public ResponseEntity<Integer> contarPorPersonalStatusEData(
            @Valid @RequestBody ReqContarAgendamentoPorPersonalStatusDataDto dto
    ) {
        Integer contagem = agendamentoService.buscarContagemDeAgendamentosPorPersonalStatusData(
                dto.status(),
                dto.data()
        );
        return ResponseEntity.ok(contagem);
    }

    @Operation(
            summary = "Conta as consultorias realizadas por mês do personal logado",
            description = "Retorna a quantidade de consultorias realizadas nos últimos meses do personal logado."
    )
    @GetMapping("/consultoria-realizadas/{quantidadeMeses}")
    public ResponseEntity<List<ResListarConsultoriasRealizadasDTO>> listarConsultoriasRealizadasMeses(
            @PathVariable Integer quantidadeMeses
    ) {
        return ResponseEntity.ok(agendamentoService.listarConsultoriasRealizadasMes(quantidadeMeses));
    }


    @GetMapping("/dia-semana/{diaSemana}")
    public ResponseEntity<Page<ResAgendamentoByDayOfWeekDto>>
    checkDiaSemanaAgendamentos(@PageableDefault(sort = "data") Pageable pageable,
                               @PathVariable DiaSemana diaSemana){
        Page<ResAgendamentoByDayOfWeekDto> agendamentos = agendamentoService.checkDiaSemanaAgendamentos(
                pageable,
                diaSemana
        );
        if(agendamentos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{alunoId}/resumos")
    public ResponseEntity<Page<ResResumoDTO>> getAgendamentosWithResume(
            @PageableDefault(size = 20) Pageable pageable,
            @PathVariable Long alunoId
    ) {
        return ResponseEntity.ok(agendamentoService.pegarAgendamentosComResumoPorAlunoId(alunoId, pageable));
    }
}