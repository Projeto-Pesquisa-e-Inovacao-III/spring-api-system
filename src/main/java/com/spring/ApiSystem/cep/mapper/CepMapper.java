package com.spring.ApiSystem.cep.mapper;

import com.spring.ApiSystem.cep.dto.response.*;
import com.spring.ApiSystem.cep.CEP;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CepMapper {
    CEP toEntity(CEPDto CEPDto);
    CEP toEntity(DadosCepDTO dadosCepDTO);

    CEPDto toDto(CEP CEP);
    ResBuscarAgendamentosPersonalPorIdCEPDto buscarSolicitacoesPersonalPorCEP(CEP cep);
    ResBuscarAgendamentosAlunosPorIdCEPDto buscarSolicitacoesAlunolPorCEP(CEP cep);

    ResBuscarSolicitacoesPorPersonalCEPDto buscarAgendamentosPorId(CEP cep);
    DadosCepDTO toDadosCepDTO(CEP CEP);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CEP partialUpdate(CEPDto CEPDto, @MappingTarget CEP CEP);
}
