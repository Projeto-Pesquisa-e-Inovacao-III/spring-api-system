package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.horariopersonal.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResDiaDisponibilidadeDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResHorarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Disponibilidade Personal",
description = "Operações para gerenciar os horários e a disponibilidade do Personal")
@RequestMapping("/api/personais")
@RestController
public class DisponibilidadePersonalController {

    private final DisponibilidadePersonalService disponibilidadeService;
    public DisponibilidadePersonalController(DisponibilidadePersonalService disponibilidadeService) {
        this.disponibilidadeService = disponibilidadeService;
    }


    @Operation(summary = "Criar um novo horário",
            description = "Cadastra um novo bloco de horário (disponibilidade, intervalo ou restrito) para um personal.")
    @PostMapping("/{personalId}/horarios")
    public ResponseEntity<ResHorarioDTO> criarHorario(@PathVariable Long personalId, @Valid @RequestBody ReqHorarioDTO request) {

        ResHorarioDTO horarioCriado = disponibilidadeService.criarHorario(personalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioCriado);
    }

    @Operation(summary = "Listar todos horários cadastrados",
            description = "Retorna uma lista de todos os blocos de horário de um personal.")
    @GetMapping("/{personalId}/horarios")
    public ResponseEntity<List<ResHorarioDTO>> listarHorariosPorPersonal(@PathVariable Long personalId) {

        List<ResHorarioDTO> horarios = disponibilidadeService.listarHorariosPorPersonal(personalId);
        return ResponseEntity.ok(horarios);
    }

    @Operation(summary = "Consultar slots de horários disponíveis",
            description = "Retorna os slots de tempo livres de um personal, subtraindo intervalos e restrições. Pode ser filtrado por um dia da semana.")
    @GetMapping("/{personalId}/horarios/disponiveis")
    public ResponseEntity<List<ResDiaDisponibilidadeDTO>> obterHorariosDisponiveis(@PathVariable Long personalId, @RequestParam(required = false) DiaSemana diaSemana) {

        List<ResDiaDisponibilidadeDTO> disponibilidade = disponibilidadeService.obterHorariosDisponiveis(personalId, diaSemana);
        return ResponseEntity.ok(disponibilidade);
    }

    @Operation(summary = "Atualizar um horário existente",
            description = "Altera um bloco de horário existente (dia, tipo ou horas) pelo id.")
    @PutMapping("/horarios/{horarioId}")
    public ResponseEntity<ResHorarioDTO> atualizarHorario(@PathVariable Long horarioId, @Valid @RequestBody ReqHorarioDTO request) {

        ResHorarioDTO horarioAtualizado = disponibilidadeService.atualizarHorario(horarioId, request);
        return ResponseEntity.ok(horarioAtualizado);
    }

    @Operation(summary = "Deletar um horário",
            description = "Remove um bloco de horário pelo id.")
    @DeleteMapping("/horarios/{horarioId}")
    public ResponseEntity<Void> deletarHorario(
            @PathVariable Long horarioId) {

        disponibilidadeService.deletarHorario(horarioId);
        return ResponseEntity.noContent().build();
    }
}
