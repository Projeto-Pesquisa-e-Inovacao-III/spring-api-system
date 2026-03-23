package com.spring.ApiSystem.domain.disponibilidade;


import com.spring.ApiSystem.domain.disponibilidade.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResHorarioDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResSlotDisponivelDTO;
import com.spring.ApiSystem.domain.disponibilidade.mapper.DisponibilidadePersonalMapper;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Disponibilidade Personal",
description = "Operações para gerenciar os horários e a disponibilidade do Personal")
@RequestMapping("/api/personais")
@RestController
public class DisponibilidadePersonalController {

    private final PersonalService personalService;
    private final DisponibilidadePersonalMapper disponibilidadePersonalMapper;

    public DisponibilidadePersonalController(PersonalService personalService,
                                             DisponibilidadePersonalMapper disponibilidadePersonalMapper,
                                             DisponibilidadePersonalService disponibilidadePersonalService) {
        this.personalService = personalService;
        this.disponibilidadePersonalMapper = disponibilidadePersonalMapper;
    }


    @Operation(summary = "Consultar slots de horários disponíveis",
            description = "Retorna os slots de tempo livres de um personal, subtraindo intervalos e restrições.")
    @GetMapping("/{personalId}/horarios-disponiveis")
    public ResponseEntity<List<ResSlotDisponivelDTO>> obterHorariosDisponiveis(@PathVariable Long personalId,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                                                               @RequestParam TipoAula tipoAula){

        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é permitido consultar horários disponíveis para datas passadas.");
        }

        List<ResSlotDisponivelDTO> slots = personalService.consultarDisponibilidade(personalId, data, tipoAula);
        return ResponseEntity.ok(slots);

    }

    @Operation(summary = "Atualizar um horário existente",
            description = "Altera um bloco de horário existente (dia, tipo ou horas) pelo id.")
    @PutMapping("/horarios/{horarioId}")
    public ResponseEntity<ResHorarioDTO> atualizarHorarios(@PathVariable Long horarioId, @Valid @RequestBody ReqHorarioDTO request) {

        ResHorarioDTO horarioAtualizado = personalService.atualizarHorarioDisponibilidade(horarioId, request);
        return ResponseEntity.ok(horarioAtualizado);
    }

    @Operation(summary = "Cronograma do Personal Logado",
    description = "Retorna o cronograma do personal logado")
    @GetMapping("/me/cronograma")
    public ResponseEntity<List<ResHorarioDTO>> pegarCronograma(){
        List<ResHorarioDTO> cronograma = disponibilidadePersonalMapper.toResHorarioDto(personalService.pegarCronogramaDoPersonal());

        return ResponseEntity.ok(cronograma);
    }

    @GetMapping("/change-activation/{diaSemana}")
    public ResponseEntity<List<ResHorarioDTO>> changeActivation(@PathVariable DiaSemana diaSemana) {
        return ResponseEntity.ok(disponibilidadePersonalMapper.toResHorarioDto(
                personalService.changeActivation(diaSemana)
        ));
    }
}
