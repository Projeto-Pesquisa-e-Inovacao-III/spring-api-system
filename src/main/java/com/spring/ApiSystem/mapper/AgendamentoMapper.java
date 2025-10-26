package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.agendamento.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.dto.agendamento.response.BuscarAgendamentoPorIdDTO;
import com.spring.ApiSystem.model.*;
import com.spring.ApiSystem.service.AlunoService;
import com.spring.ApiSystem.service.PersonalService;
import com.spring.ApiSystem.service.ProdutoContratadoService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.ApiSystem.dto.agendamento.response.ListarAgendamentoPersonalDto;
import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.dto.agendamento.response.ListarAgendamentoAlunoDto;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, UsuarioMapper.class, ProdutoContratadoMapper.class})
public abstract class  AgendamentoMapper {


    @Autowired
    protected PersonalService personalService;

    @Autowired
    protected AlunoService alunoService;

    @Autowired
    protected ProdutoContratadoService produtoContratadoService;

    @Mapping(target = "endereco",ignore = true)
    @Mapping(target = "personal",source = "personalId", qualifiedByName = "idToPersonal")
    @Mapping(target = "aluno",source = "alunoId", qualifiedByName = "idToAluno")
    @Mapping(target = "produtoContratado",source = "produtoContratadoId", qualifiedByName = "idToProdutoContratado")
    public abstract  Agendamento toEntity(CriarAgendamentoDTO dto);


    @Named("idToPersonal")
    protected Personal dToPersonal (Integer id){
        return personalService.findById(id);
    }

    @Named("idToAluno")
    protected Aluno idToAluno (Long id){
        return alunoService.findById(id);
    }

    @Named("idToProdutoContratado")
    protected ProdutoContratado idToProdutoContratado (Integer id){
        return produtoContratadoService.findById(id);
    }

    @Mapping(source = "produtoContratado.produtoExibicao.titulo", target = "aula.titulo")
    @Mapping(source = "produtoContratado.produtoExibicao.tipoAula", target = "aula.tipoAula")
    public abstract BuscarAgendamentoPorIdDTO toDTO(Agendamento agendamento);

    @Mapping(source = "produtoContratado.produtoExibicao.titulo", target = "aula.titulo")
    @Mapping(source = "produtoContratado.produtoExibicao.tipoAula", target = "aula.tipoAula")
    public abstract ListarAgendamentoAlunoDto toListarAgendamentoAlunoDto(Agendamento agendamento);

    public abstract Agendamento toEntity(ListarAgendamentoAlunoDto listarAgendamentoAlunoDto);

    public abstract Agendamento toEntity(ListarAgendamentoPersonalDto listarAgendamentoPersonalDto);

    @Mapping(source = "produtoContratado.produtoExibicao.titulo", target = "aula.titulo")
    @Mapping(source = "produtoContratado.produtoExibicao.tipoAula", target = "aula.tipoAula")
    public abstract ListarAgendamentoPersonalDto toListarAgendamentoPersonalDto(Agendamento agendamento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Agendamento partialUpdate(ListarAgendamentoPersonalDto listarAgendamentoPersonalDto, @MappingTarget Agendamento agendamento);
}
