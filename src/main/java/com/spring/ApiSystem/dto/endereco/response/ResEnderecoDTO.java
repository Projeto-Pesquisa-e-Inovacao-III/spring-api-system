package com.spring.ApiSystem.dto.endereco.response;

import com.spring.ApiSystem.model.CEP;

public record ResEnderecoDTO(
    Long id,
    String numero,
    String complemento,
    String unidade,
    String tipo,
    CEP cep
) {}
