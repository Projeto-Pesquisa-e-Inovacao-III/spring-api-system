package com.spring.ApiSystem.domain.cep.mapper;

import com.spring.ApiSystem.domain.cep.CEP;
import com.spring.ApiSystem.domain.cep.dto.response.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CepMapper {
    CEP toEntity(CEPDto CEPDto);
    CEP toEntity(DadosCepDTO dadosCepDTO);

    CEPDto toDto(CEP CEP);
    ResBuscarAgendamentosPersonalPorIdCEPDto buscarSolicitacoesPersonalPorCEP(CEP cep);
    ResBuscarAgendamentosAlunosPorIdCEPDto buscarSolicitacoesAlunolPorCEP(CEP cep);

    ResBuscarSolicitacoesPorPersonalCEPDto buscarAgendamentosPorId(CEP cep);
    @Mapping(source = "id", target = "cep")
    DadosCepDTO toDadosCepDTO(CEP CEP);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CEP partialUpdate(CEPDto CEPDto, @MappingTarget CEP CEP);
}
