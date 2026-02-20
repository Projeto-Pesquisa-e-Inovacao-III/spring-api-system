package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.endereco.dto.response.ResBuscarSolicitacoesPorAlunoEnderecoDTO;
import com.spring.ApiSystem.endereco.dto.response.ResBuscarSolicitacoesPorPersonalEnderecoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.telefone.dto.response.ResBuscarSolicitacoesPorAlunoTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResBuscarSolicitacoesPorPersonalTelefoneDTO;

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


