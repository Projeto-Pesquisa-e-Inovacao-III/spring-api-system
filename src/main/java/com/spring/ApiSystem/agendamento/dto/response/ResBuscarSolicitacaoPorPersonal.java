package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.endereco.dto.response.ResBuscarSolicitacoesPorPersonalEnderecoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.response.ResBuscarSolicitacoesPorPersonalTelefoneDTO;


import java.time.LocalDateTime;

public record ResBuscarSolicitacaoPorPersonal(
        Long agendamentoId,
        TipoAula tipoAula,
        String nome,
        ResBuscarSolicitacoesPorPersonalTelefoneDTO telefone,
        String idade,
        String foto,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        ResBuscarSolicitacoesPorPersonalEnderecoDTO endereco,
        String status
) {}


