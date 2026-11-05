package com.spring.ApiSystem.domain.resumoagendamento;

import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/resumo-agendamento")
public class ResumoAgendamentoController {
    private final ResumoAgendamentoService resumoAgendamentoService;

    public ResumoAgendamentoController(ResumoAgendamentoService resumoAgendamentoService) {
        this.resumoAgendamentoService = resumoAgendamentoService;
    }

    @GetMapping("/{alunoId}")
    public ResponseEntity<PaginaCursor<ResResumoAgendamentoAlunoDTO>> consultarResumoAluno(
            @PathVariable Long alunoId,
            @RequestParam(required = false) Long proximoCursor,
            @RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(resumoAgendamentoService.consultarResumoAluno(alunoId, proximoCursor, limit));
    }

    @GetMapping("/listar-grupos-musculares")
    public ResponseEntity<GrupoMuscular[]> listarGrupoMusculares() {
        return ResponseEntity.ok(resumoAgendamentoService.listarGruposMusculares());
    }
}
