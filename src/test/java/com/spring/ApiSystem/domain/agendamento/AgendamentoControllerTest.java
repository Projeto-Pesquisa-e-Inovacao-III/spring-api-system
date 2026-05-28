package com.spring.ApiSystem.domain.agendamento;

import com.spring.ApiSystem.domain.resumoagendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.shared.service.PageableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AgendamentoController - Confirmar Conclusao")
class AgendamentoControllerTest {

    @Mock
    private AgendamentoService agendamentoService;

    @Mock
    private PageableService pageableService;

    @InjectMocks
    private AgendamentoController agendamentoController;

    @Test
    @DisplayName("PUT /agendamentos/{id}/confirmar-conclusao - Deve retornar 204")
    void deveConfirmarConclusaoComNoContent() {
        ReqCadastrarResumoAgendamentoDTO dto = new ReqCadastrarResumoAgendamentoDTO(
                "Resumo da aula",
                List.of(GrupoMuscular.PEITO)
        );

        ResponseEntity<Void> response = agendamentoController.confirmarConclusao(1L, dto);

        assertEquals(204, response.getStatusCode().value());
        verify(agendamentoService).confirmarConclusao(1L, dto);
    }
}

