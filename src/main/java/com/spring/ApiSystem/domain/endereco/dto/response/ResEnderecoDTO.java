package com.spring.ApiSystem.domain.endereco.dto.response;
import com.spring.ApiSystem.domain.cep.CEP;

public record ResEnderecoDTO(
        Long id,
        String numero,
        String complemento,
        String unidade,
        String tipo,
        CEP cep
) {}