package com.spring.ApiSystem.horariopersonal.mapper;

import com.spring.ApiSystem.horariopersonal.DisponibilidadePersonal;
import com.spring.ApiSystem.horariopersonal.dto.response.ResHorarioDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DisponibilidadePersonalMapper {
    List<ResHorarioDTO> toResHorarioDto(List<DisponibilidadePersonal> disponibilidadePersonals);
}
