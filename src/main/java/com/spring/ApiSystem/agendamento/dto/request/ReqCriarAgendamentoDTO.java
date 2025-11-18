package com.spring.ApiSystem.agendamento.dto.request;

import com.spring.ApiSystem.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
public record ReqCriarAgendamentoDTO(

        @NotNull(message = "A data é obrigatória")
        LocalDateTime data,

        String descricao,

        @Valid ReqCadastrarEnderecoDTO novoEndereco,

        @NotNull(message = "O personal é obrigatório")
        Long personalId,

        @NotNull(message = "O produto contratado é obrigatório")
        TipoAula tipoAulaProdutoContratado
) {

}
