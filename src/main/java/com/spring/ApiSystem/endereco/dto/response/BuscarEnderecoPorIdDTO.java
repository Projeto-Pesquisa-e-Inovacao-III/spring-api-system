package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.cep.dto.response.DadosCepDTO;

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