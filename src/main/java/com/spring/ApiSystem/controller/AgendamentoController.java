package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.repository.AgendamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoService;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoController(AgendamentoRepository agendamentoService,
                                 AgendamentoMapper agendamentoMapper) {
        this.agendamentoService = agendamentoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @GetMapping
    public ResponseEntity<?> listAll(){
        List<Agendamento> agendamentos = agendamentoService.findAll();



        return new ResponseEntity<>(agendamentos.stream().map(agendamentoMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

}
