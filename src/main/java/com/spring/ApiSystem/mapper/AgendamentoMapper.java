package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.agendamento.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.dto.agendamento.response.BuscarAgendamentoPorIdDTO;
import com.spring.ApiSystem.model.*;
import com.spring.ApiSystem.service.AlunoService;
import com.spring.ApiSystem.service.EnderecoService;
import com.spring.ApiSystem.service.PersonalService;
import com.spring.ApiSystem.service.ProdutoContratadoService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",uses = {EnderecoMapper.class})
public abstract  class  AgendamentoMapper {


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
    protected Aluno idToAluno (Integer id){
        return alunoService.findById(id);
    }

    @Named("idToProdutoContratado")
    protected ProdutoContratado idToProdutoContratado (Integer id){
        return produtoContratadoService.findById(id);
    }

    @Mapping(source = "produtoContratado.produtoExibicao.titulo", target = "aula.titulo")
    @Mapping(source = "produtoContratado.produtoExibicao.tipoAula", target = "aula.tipoAula")
    public abstract BuscarAgendamentoPorIdDTO toDTO(Agendamento agendamento);

}
