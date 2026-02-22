package com.spring.ApiSystem.domain.horariopersonal.mapper;


import com.spring.ApiSystem.domain.horariopersonal.DisponibilidadePersonal;
import com.spring.ApiSystem.domain.horariopersonal.dto.response.ResHorarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DisponibilidadePersonalMapper {

    @Mapping(target = "personalId", source = "personal.id")
    ResHorarioDTO toResHorarioDto(DisponibilidadePersonal disponibilidadePersonal);

    List<ResHorarioDTO> toResHorarioDto(List<DisponibilidadePersonal> disponibilidadePersonals);
}
