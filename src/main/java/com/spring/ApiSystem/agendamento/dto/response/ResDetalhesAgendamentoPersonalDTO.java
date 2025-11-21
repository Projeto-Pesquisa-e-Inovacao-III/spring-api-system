package com.spring.ApiSystem.agendamento.dto.response;

import java.time.LocalDateTime;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

public record ResDetalhesAgendamentoPersonalDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int duracaoMinutos,
        AgendamentoStatus status,
        AlunoDadosBasico aluno,
        String endereco,
        TipoAula tipoAula,
        String local,
        String descricao
) {
    public record AlunoDadosBasico(
            Long id,
            String nome,
            String idade,
            String avatarUrl
    ) {}
}
