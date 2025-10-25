package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.mapper.AgendamentoMapper;
import com.spring.ApiSystem.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoMapper agendamentoMapper) {
        this.agendamentoService = agendamentoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> criarAgendamento(){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping
    public ResponseEntity<?> listarTodosAgendamentosPorId(){}

    @GetMapping
    ResponseEntity<?> listarTodasDatasPorId(){}

    @PatchMapping
    public ResponseEntity<?> atualizarAgendamento(){}

    @PatchMapping
    public ResponseEntity<?> reagendarAgendamento(){}

    @GetMapping
    public ResponseEntity<?> buscarDadosDoAgendamentoPorId(){}

    @PatchMapping
    public ResponseEntity<?> cancelarAgendamento(){}

    //Filtrar essa porra por nome e status
    @GetMapping
    public ResponseEntity<?> buscarAgendamentosStatusOuNome(){}
}
