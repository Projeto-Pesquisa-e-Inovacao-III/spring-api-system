package com.spring.ApiSystem.historicoagendamento.mapper;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.historicoagendamento.HistoricoAgendamento;
import com.spring.ApiSystem.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoricoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "motivo", source = "dto.descricao")
    @Mapping(target = "dataHora", source = "dto.data")
    @Mapping(target = "dataFim", source = "dto.dataFim")
    @Mapping(target = "tipoAula", source = "dto.tipoAula")
    @Mapping(target = "status", source = "dto.status")
    @Mapping(target = "usuario", source = "dto.usuario")
    @Mapping(target = "endereco", source = "dto.endereco")
    @Mapping(target = "agendamento", source = "agendamento")
    HistoricoAgendamento toEntity(ReqCadastrarHistoricoAgendamentoDTO dto, Agendamento agendamento);
}


