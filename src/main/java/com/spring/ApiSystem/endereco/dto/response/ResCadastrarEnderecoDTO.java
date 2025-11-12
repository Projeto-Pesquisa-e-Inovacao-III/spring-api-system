package com.spring.ApiSystem.endereco.dto.response;
import com.spring.ApiSystem.cep.CEP;

public record ResCadastrarEnderecoDTO(
        String numero,
        String complemento,
        String unidade,
        String tipo,
        CEP cep
) {}