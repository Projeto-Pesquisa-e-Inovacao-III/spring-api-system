package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.horariopersonal.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResDiaDisponibilidadeDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResSlotDisponivelDTO;
import com.spring.ApiSystem.horariopersonal.mapper.DisponibilidadePersonalMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Disponibilidade Personal",
description = "Operações para gerenciar os horários e a disponibilidade do Personal")
@RequestMapping("/personais")
@RestController
public class DisponibilidadePersonalController {

    private final DisponibilidadePersonalService disponibilidadeService;
    private final DisponibilidadePersonalMapper disponibilidadePersonalMapper;

    public DisponibilidadePersonalController(DisponibilidadePersonalService disponibilidadeService, DisponibilidadePersonalMapper disponibilidadePersonalMapper) {
        this.disponibilidadeService = disponibilidadeService;
        this.disponibilidadePersonalMapper = disponibilidadePersonalMapper;
    }


    @Operation(summary = "Consultar slots de horários disponíveis",
            description = "Retorna os slots de tempo livres de um personal, subtraindo intervalos e restrições.")
    @GetMapping("/{personalId}/horarios-disponiveis")
    public ResponseEntity<List<ResSlotDisponivelDTO>> obterHorariosDisponiveis(@PathVariable Long personalId,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){

        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é permitido consultar horários disponíveis para datas passadas.");
        }

        List<ResSlotDisponivelDTO> slots = disponibilidadeService.obterHorariosDisponiveis(personalId, data);
        return ResponseEntity.ok(slots);

    }

    @Operation(summary = "Atualizar um horário existente",
            description = "Altera um bloco de horário existente (dia, tipo ou horas) pelo id.")
    @PutMapping("/horarios/{horarioId}")
    public ResponseEntity<ResHorarioDTO> atualizarHorarios(@PathVariable Long horarioId, @Valid @RequestBody ReqHorarioDTO request) {

        ResHorarioDTO horarioAtualizado = disponibilidadeService.atualizarHorarios(horarioId, request);
        return ResponseEntity.ok(horarioAtualizado);
    }

    @Operation(summary = "Cronograma do Personal Logado",
    description = "Retorna o cronograma do personal logado")
    @GetMapping("/me/cronograma")
    public ResponseEntity<List<ResHorarioDTO>> pegarCronograma(){
        List<ResHorarioDTO> cronograma = disponibilidadePersonalMapper.toResHorarioDto(disponibilidadeService.pegarCronograma());
        return ResponseEntity.ok(cronograma);
    }
}
