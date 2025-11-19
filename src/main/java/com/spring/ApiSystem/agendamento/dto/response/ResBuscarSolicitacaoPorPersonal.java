package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.endereco.Endereco;

import java.time.LocalDate;
import java.time.LocalTime;

public record ResBuscarSolicitacaoPorPersonal(
        Long id,
        String status,
        String nomeCliente,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        String tipo,
        Integer idade,
        String celular,
        String local,
        String enderecoLogradouro,
        String enderecoNumero,
        String enderecoBairro,
        String enderecoCidade,
        String enderecoUf,
        String descricao,
        String avatarUrl
) {}

