package com.spring.ApiSystem.domain.historicoagendamento;

import com.spring.ApiSystem.domain.agendamento.dto.response.ResTotalAgendamentoByStatusDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Histórico de Agendamento", description = "Operações relacionadas ao histórico de agendamento")
@RestController
@RequestMapping("/api/historico-agendamento")
public class HistoricoAgendamentoController {
    private final HistoricoAgendamentoService historicoAgendamentoService;

    public HistoricoAgendamentoController(HistoricoAgendamentoService historicoAgendamentoService) {
        this.historicoAgendamentoService = historicoAgendamentoService;
    }

    @GetMapping("/total-status")
    public ResponseEntity<ResTotalAgendamentoByStatusDto> countTotalByStatus(){
        return ResponseEntity.ok(historicoAgendamentoService.countTotalStatusAgendamentoByPersonal());
    }

}
