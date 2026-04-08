package com.spring.ApiSystem.domain.disponibilidade.mapper;


import com.spring.ApiSystem.domain.disponibilidade.DisponibilidadePersonal;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResDiaSemanaAndAtivoDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResHorarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DisponibilidadePersonalMapper {

    @Mapping(target = "personalId", source = "personal.id")
    ResHorarioDTO toResHorarioDto(DisponibilidadePersonal disponibilidadePersonal);

    List<ResHorarioDTO> toResHorarioDto(List<DisponibilidadePersonal> disponibilidadePersonals);

    ResDiaSemanaAndAtivoDTO toResDiaSemanaAndAtivoDTO(DisponibilidadePersonal disponibilidadePersonal);
    List<ResDiaSemanaAndAtivoDTO> toResDiaSemanaAndAtivoDTO(List<DisponibilidadePersonal> disponibilidadePersonals);
}
