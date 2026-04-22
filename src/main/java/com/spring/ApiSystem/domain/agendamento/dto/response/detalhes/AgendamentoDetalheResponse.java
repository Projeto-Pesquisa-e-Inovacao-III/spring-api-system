package com.spring.ApiSystem.domain.agendamento.dto.response.detalhes;

import java.time.LocalDateTime;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.endereco.dto.response.ResAgendementoDadosEnderecoAlunoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;


public interface AgendamentoDetalheResponse {

    Long id();
    LocalDateTime dataInicio();
    LocalDateTime dataFim();
    int duracaoMinutos();
    AgendamentoStatus status();
    ResAgendementoDadosEnderecoAlunoDTO endereco();
    TipoAula tipoAula();
    String descricao();
}