package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ResumoAgendamentoController")
class ResumoAgendamentoControllerTest {

    @Mock
    private ResumoAgendamentoService resumoAgendamentoService;

    @InjectMocks
    private ResumoAgendamentoController resumoAgendamentoController;

    @Test
    @DisplayName("GET /resumo-agendamento/{alunoId} - Deve retornar 200 com pagina")
    void deveRetornarPaginaDeResumo() {
        Long alunoId = 1L;
        Long proximoCursor = 10L;
        int limit = 2;
        PaginaCursor<ResResumoAgendamentoAlunoDTO> pagina = new PaginaCursor<>(
                List.of(new ResResumoAgendamentoAlunoDTO("Aluno", "Resumo", List.of(GrupoMuscular.PEITO))),
                proximoCursor
        );

        when(resumoAgendamentoService.consultarResumoAluno(alunoId, proximoCursor, limit))
                .thenReturn(pagina);

        ResponseEntity<PaginaCursor<ResResumoAgendamentoAlunoDTO>> response =
                resumoAgendamentoController.consultarResumoAluno(alunoId, proximoCursor, limit);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(pagina, response.getBody());
        verify(resumoAgendamentoService).consultarResumoAluno(alunoId, proximoCursor, limit);
    }

    @Test
    @DisplayName("GET /resumo-agendamento/listar-grupos-musculares - Deve retornar 200")
    void deveListarGruposMusculares() {
        GrupoMuscular[] grupos = GrupoMuscular.values();

        when(resumoAgendamentoService.listarGruposMusculares()).thenReturn(grupos);

        ResponseEntity<GrupoMuscular[]> response = resumoAgendamentoController.listarGrupoMusculares();

        assertEquals(200, response.getStatusCode().value());
        assertArrayEquals(grupos, response.getBody());
        verify(resumoAgendamentoService).listarGruposMusculares();
    }
}

