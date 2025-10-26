package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.mapper.AgendamentoMapper;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.service.AgendamentoService;
import com.spring.ApiSystem.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("")
    public ResponseEntity<?> criarAgendamento(){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping
    public ResponseEntity<Page<?>> getAgendamentos(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<?> agendamentos = agendamentoService
                .pegarTodosAgendamentosDesseUsuario(userDetails.getUsername(), page, size);

        return ResponseEntity.ok(agendamentos);
    }
//
//    @GetMapping
//    ResponseEntity<?> listarTodasDatasPorId(){}
//
//    @PatchMapping
//    public ResponseEntity<?> atualizarAgendamento(){}
//
//    @PatchMapping
//    public ResponseEntity<?> reagendarAgendamento(){}
//
//    @GetMapping
//    public ResponseEntity<?> buscarDadosDoAgendamentoPorId(){}
//
//    @PatchMapping
//    public ResponseEntity<?> cancelarAgendamento(){}
//
//    //Filtrar essa porra por nome e status
//    @GetMapping
//    public ResponseEntity<?> buscarAgendamentosStatusOuNome(){}
}
