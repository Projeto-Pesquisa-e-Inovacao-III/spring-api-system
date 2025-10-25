package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.agendamento.response.ListAllAgendamentoDto;
import com.spring.ApiSystem.model.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agendamentoState", ignore = true)
    Agendamento toEntity(ListAllAgendamentoDto agendamentoDto);

    ListAllAgendamentoDto toDto(Agendamento agendamento);
}
