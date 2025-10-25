package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.repository.AgendamentoRepository;
import com.spring.ApiSystem.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoService;

    public AgendamentoController(AgendamentoRepository agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<?> listAll(){
        List<Agendamento> agendamentos = agendamentoService.findAll();

        for (Agendamento agendamento : agendamentos) {
            System.out.println("Agendamento vindo do banco: " + agendamento);
        }

        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

}
