package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumo-agendamento")
public class ResumoAgendamentoController {
    private final ResumoAgendamentoService resumoAgendamentoService;

    public ResumoAgendamentoController(ResumoAgendamentoService resumoAgendamentoService) {
        this.resumoAgendamentoService = resumoAgendamentoService;
    }

    @GetMapping("/listar-grupos-musculares")
    public ResponseEntity<GrupoMuscular[]> listarGrupoMusculares() {
        return ResponseEntity.ok(resumoAgendamentoService.listarGruposMusculares());
    }
}
