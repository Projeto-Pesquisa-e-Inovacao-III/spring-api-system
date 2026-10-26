package com.spring.ApiSystem.domain.agendamento.dto.response;

import com.spring.ApiSystem.domain.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ResDadoExtraidoDTO(
        String personalNome,
        LocalDateTime data,
        TipoAula tipoAula,
        ReqCadastrarEnderecoDTO novoEndereco
) {}
