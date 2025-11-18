package com.spring.ApiSystem.agendamento.mapper;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.agendamento.dto.request.ReqCadastrarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReqReagendarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.response.ResAgendamentoAlunoOverviewDTO;
import com.spring.ApiSystem.agendamento.dto.response.ResAgendamentoPersonalOverviewDTO;
import com.spring.ApiSystem.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import com.spring.ApiSystem.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;

import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, UsuarioMapper.class, ProdutoContratadoMapper.class})
public interface   AgendamentoMapper {

    @Mapping(target = "personal.id", source = "personalId")
    @Mapping(target = "endereco", source = "novoEndereco")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "agendamentoState", ignore = true)
    Agendamento toEntity(ReqCadastrarAgendamentoDTO reqCadastrarAgendamentoDTO);
    Agendamento toEntity(ReqReagendarAgendamentoDTO reqReagendarAgendamentoDTO);
    @Mapping(target = "alunoNome", source = "aluno.nome")
    @Mapping(target = "personalNome", source = "personal.nome")
    @Mapping(target = "produtoContratadoNome", source = "produtoContratado.produtoExibicao.titulo")
    ResCriarAgendamentoDTO toResCriarAgendamentoDTO(Agendamento agendamento);
    ReqCadastrarHistoricoAgendamentoDTO toReqCriarHistoricoAgendamentoDTO(Agendamento agendamento);

    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "agendamentoStatus", source = "status")
    @Mapping(target = "datafim", source = "dataFim")
    @Mapping(target = "alunoNome", source = "aluno.nome")
    @Mapping(target = "personalNome", source = "personal.nome")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    ResAgendamentoAlunoOverviewDTO toResAgendamentoAlunoOverviewDTO(Agendamento agendamento);

    List<ResAgendamentoAlunoOverviewDTO> toResAgendamentoAlunoOverviewDTOList(List<Agendamento> agendamentos);

    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "agendamentoStatus", source = "status")
    @Mapping(target = "datafim", source = "dataFim")
    @Mapping(target = "alunoNome", source = "aluno.nome")
    @Mapping(target = "personalNome", source = "personal.nome")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    ResAgendamentoPersonalOverviewDTO toResAgendamentoPersonalOverviewDTO(Agendamento agendamento);

    List<ResAgendamentoPersonalOverviewDTO> toResAgendamentoPersonalOverviewDTOList(List<Agendamento> agendamentos);

}


