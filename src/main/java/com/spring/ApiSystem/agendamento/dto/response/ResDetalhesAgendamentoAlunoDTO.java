
package com.spring.ApiSystem.agendamento.dto.response;

import java.time.LocalDateTime;

import com.spring.ApiSystem.cep.dto.response.ResBuscarAgendamentosAlunosPorIdCEPDto;
import com.spring.ApiSystem.cep.dto.response.ResBuscarAgendamentosPersonalPorIdCEPDto;
import com.spring.ApiSystem.endereco.dto.response.ResAgendementoDadosEnderecoAlunoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

public record ResDetalhesAgendamentoAlunoDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int duracaoMinutos,
        AgendamentoStatus status,
        ResDetalhesAgendamentoPersonal personal,
        ResAgendementoDadosEnderecoAlunoDTO endereco,
        TipoAula tipoAula,
        String descricao
) {
    public record ResDetalhesAgendamentoPersonal(
            Long id,
            String nome,
            String idade,
            String avatarUrl
    ) {}
}
