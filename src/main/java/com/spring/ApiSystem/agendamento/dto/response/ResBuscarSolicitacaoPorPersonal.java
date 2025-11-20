package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.telefone.Telefone;


import java.time.LocalDateTime;

public record ResBuscarSolicitacaoPorPersonal(
        Long agendamentoId,
        TipoAula tipoAula,
        String nome,
        Telefone telefone,
        String idade,
        String foto,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Endereco endereco,
        String status
) {}


