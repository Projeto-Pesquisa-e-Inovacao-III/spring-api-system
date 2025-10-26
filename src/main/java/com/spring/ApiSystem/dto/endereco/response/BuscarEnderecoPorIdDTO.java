package com.spring.ApiSystem.dto.endereco.response;

import com.spring.ApiSystem.dto.cep.response.DadosCepDTO;

import java.time.LocalDateTime;

public record BuscarEnderecoPorIdDTO(
        Long id,
        String numero,
        String complemento,
        String unidade,
        String tipo,
        DadosCepDTO cep,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}

