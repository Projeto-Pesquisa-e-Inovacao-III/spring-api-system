
package com.spring.ApiSystem.agendamento.dto.response;

import java.time.LocalDateTime;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

public record ResDetalhesAgendamentoAlunoDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int duracaoMinutos,
        AgendamentoStatus status,
        PersonalDadosBasico personal,
        String endereco,
        TipoAula tipoAula,
        String local,
        String descricao
) {
    public record PersonalDadosBasico(
            Long id,
            String nome,
            String idade,
            String avatarUrl
    ) {}
}
