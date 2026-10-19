package com.spring.ApiSystem.domain.agendamento.dto.response;

public record ResDadoExtraidoDTO(
        String personalNome,
        String data,
        String tipoAula,
        String endereco
) {}
