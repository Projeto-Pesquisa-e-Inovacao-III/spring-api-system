package com.spring.ApiSystem.cep.dto.response;

public record ResBuscarAgendamentosAlunosPorIdCEPDto(
        String id,
        String logradouro,
        String bairro,
        String localidade,
        String uf
){
}
