package com.spring.ApiSystem.domain.agendamento.dto.response;


import com.spring.ApiSystem.domain.endereco.dto.response.ResBuscarSolicitacoesPorAlunoEnderecoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.telefone.dto.response.ResBuscarSolicitacoesPorAlunoTelefoneDTO;

import java.time.LocalDateTime;

public record ResBuscarSolicitacaoPorAluno(
        Long agendamentoId,
        TipoAula tipoAula,
        String nome,
        ResBuscarSolicitacoesPorAlunoTelefoneDTO telefone,
        String idade,
        String foto,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        ResBuscarSolicitacoesPorAlunoEnderecoDTO endereco,
        String status
) {}


