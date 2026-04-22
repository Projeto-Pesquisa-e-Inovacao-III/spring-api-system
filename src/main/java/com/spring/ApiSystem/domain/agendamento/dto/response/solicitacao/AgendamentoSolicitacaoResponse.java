package com.spring.ApiSystem.domain.agendamento.dto.response.solicitacao;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public interface AgendamentoSolicitacaoResponse {

    Long agendamentoId();
    TipoAula tipoAula();
    String nome();
    String idade();
    String foto();
    LocalDateTime dataInicio();
    LocalDateTime dataFim();
    String status();
}
