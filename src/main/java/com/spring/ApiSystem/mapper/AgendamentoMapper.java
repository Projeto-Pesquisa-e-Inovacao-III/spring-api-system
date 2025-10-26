package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.agendamento.response.ListarAgendamentoPersonalDto;
import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.dto.agendamento.response.ListarAgendamentoAlunoDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, UsuarioMapper.class, ProdutoContratadoMapper.class})
public interface AgendamentoMapper {

    @Mapping(source = "produtoContratado.produtoExibicao.titulo", target = "aula.titulo")
    @Mapping(source = "produtoContratado.produtoExibicao.tipoAula", target = "aula.tipoAula")
    ListarAgendamentoAlunoDto toListarAgendamentoAlunoDto(Agendamento agendamento);

    Agendamento toEntity(ListarAgendamentoAlunoDto listarAgendamentoAlunoDto);

    Agendamento toEntity(ListarAgendamentoPersonalDto listarAgendamentoPersonalDto);

    @Mapping(source = "produtoContratado.produtoExibicao.titulo", target = "aula.titulo")
    @Mapping(source = "produtoContratado.produtoExibicao.tipoAula", target = "aula.tipoAula")
    ListarAgendamentoPersonalDto toListarAgendamentoPersonalDto(Agendamento agendamento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Agendamento partialUpdate(ListarAgendamentoPersonalDto listarAgendamentoPersonalDto, @MappingTarget Agendamento agendamento);
}
