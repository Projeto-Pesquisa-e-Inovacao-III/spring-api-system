package com.spring.ApiSystem.agendamento.mapper;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.agendamento.dto.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.response.BuscarAgendamentoPorIdDTO;
import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.produtocontratado.dto.response.ResBuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.personal.PersonalService;
import com.spring.ApiSystem.produtocontratado.ProdutoContratadoService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.spring.ApiSystem.agendamento.dto.response.ListarAgendamentoPersonalDto;
import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.agendamento.dto.response.ListarAgendamentoAlunoDto;

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
        return alunoService.buscarPorId(id);
    }

    @Named("idToProdutoContratado")
    protected ResBuscarProdutoContratadoPorIdDto idToProdutoContratado (Long id){
        return produtoContratadoService.listarPorIdDto(id);
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
