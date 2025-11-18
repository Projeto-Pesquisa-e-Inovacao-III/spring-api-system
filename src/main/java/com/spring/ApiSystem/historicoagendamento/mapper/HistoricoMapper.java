package com.spring.ApiSystem.historicoagendamento.mapper;

import com.spring.ApiSystem.historicoagendamento.HistoricoAgendamento;
import com.spring.ApiSystem.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoricoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "motivo", source = "descricao")
    HistoricoAgendamento toEntity(ReqCadastrarHistoricoAgendamentoDTO dto);
}
