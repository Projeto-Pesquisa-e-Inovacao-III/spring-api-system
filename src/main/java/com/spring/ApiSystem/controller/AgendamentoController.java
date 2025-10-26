package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.dto.agendamento.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.dto.reagendamento.request.ReagendarAgendamentoDTO;
import com.spring.ApiSystem.mapper.AgendamentoMapper;
import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoMapper agendamentoMapper) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@Valid @RequestBody CriarAgendamentoDTO criarAgendamentoDTO){
        try {
            Agendamento agendamentoCriado = agendamentoService.criar(criarAgendamentoDTO);
            return new ResponseEntity<>("Agendamento realizado com sucesso.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao realizar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/reagendar")
    public ResponseEntity<?> reagendarAgendamento(@PathVariable Long id  , @Valid @RequestBody ReagendarAgendamentoDTO reagendarAgendamentoDTO){
        try {
            Agendamento agendamentoReagendado = agendamentoService.reagendar( id , reagendarAgendamentoDTO);
            return new ResponseEntity<>("Agendamento reagendado com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao reagendar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/aceitar")
    public ResponseEntity<?> aceitarAgendamento(@PathVariable Long id){
        try {
            Agendamento agendamentoAceito = agendamentoService.aceitaAgendamento(id);
            return new ResponseEntity<>("Agendamento aceito com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao aceitar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<?> recusArgendamento(@PathVariable Long id){
        try {
            Agendamento agendamentoAceito = agendamentoService.recusaAgendamento(id);
            return new ResponseEntity<>("Agendamento recusado com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao aceitar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAgendamentoPorId(@PathVariable Long id){
        try {
            return new ResponseEntity<>(  agendamentoService.buscarAgendamentoPorId(id), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
