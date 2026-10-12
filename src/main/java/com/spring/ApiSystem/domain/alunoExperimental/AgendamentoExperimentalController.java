package com.spring.ApiSystem.domain.alunoExperimental;

import com.spring.ApiSystem.domain.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.domain.alunoExperimental.dto.ReqCriarAgendamentoExperimentalRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agendamentos-experimentais")
public class AgendamentoExperimentalController {

    private final AgendamentoExperimentalService agendamentoService;

    public AgendamentoExperimentalController(AgendamentoExperimentalService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<ResCriarAgendamentoDTO> criarAgendamento(@RequestBody ReqCriarAgendamentoExperimentalRequest request) {
        ResCriarAgendamentoDTO agendamento = agendamentoService.criarAgendamentoExperimental( request);
        return ResponseEntity.ok(agendamento);
    }
}
