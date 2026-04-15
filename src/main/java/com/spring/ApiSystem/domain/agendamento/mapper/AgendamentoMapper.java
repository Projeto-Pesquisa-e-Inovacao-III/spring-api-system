package com.spring.ApiSystem.domain.agendamento.mapper;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.dto.request.ReqCadastrarAgendamentoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.request.ReqReagendarAgendamentoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResAgendamentoAlunoOverviewDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResAgendamentoByDayOfWeekDto;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.calendario.ResBuscarAgendamentoCalendarioAlunoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.calendario.ResBuscarAgendamentoCalendarioPersonalDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.detalhes.ResDetalhesAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.detalhes.ResDetalhesAgendamentoPersonalDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.overview.ResAgendamentoPersonalOverviewDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.solicitacao.ResBuscarSolicitacaoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.solicitacao.ResBuscarSolicitacaoAgendamentoPersonalDTO;
import com.spring.ApiSystem.domain.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.domain.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.telefone.mapper.TelefoneMapper;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {EnderecoMapper.class, UsuarioMapper.class, ProdutoContratadoMapper.class, TelefoneMapper.class}
)
public abstract class AgendamentoMapper {

    @Autowired
    protected JpaUserDetailsService userDetailsService;

    @Autowired
    protected TelefoneMapper telefoneMapper;

    @Autowired
    private PersonalService personalService;

    @Mapping(target = "personal.id", source = "personalId")
    @Mapping(target = "endereco", source = "novoEndereco")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "agendamentoState", ignore = true)
    public abstract Agendamento toEntity(ReqCadastrarAgendamentoDTO reqCadastrarAgendamentoDTO);

    public abstract Agendamento toEntity(ReqReagendarAgendamentoDTO reqReagendarAgendamentoDTO);

    @Mapping(target = "alunoNome", source = "aluno.nome")
    @Mapping(target = "personalNome", source = "personal.nome")
    @Mapping(target = "produtoContratadoNome", source = "produtoContratado.produtoExibicao.titulo")
    public abstract ResCriarAgendamentoDTO toResCriarAgendamentoDTO(Agendamento agendamento);

    @Mapping(target = "data", source = "data")
    @Mapping(target = "dataFim", source = "dataFim")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "descricao", source = "descricao")
    @Mapping(target = "usuario", expression = "java(obterUsuarioAtual())")
    @Mapping(target = "endereco", source = "endereco")
    public abstract ReqCadastrarHistoricoAgendamentoDTO toReqCriarHistoricoAgendamentoDTO(Agendamento agendamento);

    @Named("idToPersonal")
    protected Personal idToPersonal(Long id) {
        return personalService.buscarPorId(id);
    }

    protected Usuario obterUsuarioAtual() {
        return userDetailsService.getCurrentUser();
    }

    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "agendamentoStatus", source = "status")
    @Mapping(target = "datafim", source = "dataFim")
    @Mapping(target = "alunoNome", source = "aluno.nome")
    @Mapping(target = "personalNome", source = "personal.nome")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "caminhoFoto", source = "personal.caminhoFoto")
    public abstract ResAgendamentoAlunoOverviewDTO toResAgendamentoAlunoOverviewDTO(Agendamento agendamento);

    public abstract List<ResAgendamentoAlunoOverviewDTO> toResAgendamentoAlunoOverviewDTOList(List<Agendamento> agendamentos);

    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "agendamentoStatus", source = "status")
    @Mapping(target = "datafim", source = "dataFim")
    @Mapping(target = "alunoNome", source = "aluno.nome")
    @Mapping(target = "personalNome", source = "personal.nome")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "caminhoFoto", source = "aluno.caminhoFoto")
    public abstract ResAgendamentoPersonalOverviewDTO toResAgendamentoPersonalOverviewDTO(Agendamento agendamento);

    public abstract List<ResAgendamentoPersonalOverviewDTO> toResAgendamentoPersonalOverviewDTOList(List<Agendamento> agendamentos);

    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "nome", source = "aluno.nome")
    @Mapping(target = "telefone", expression = "java(telefoneMapper.buscarSolicitacoesPorPersonalTelefone(telefone))")
    @Mapping(target = "idade", expression = "java(calcularIdade(agendamento.getAluno().getDataNascimento()))")
    @Mapping(target = "foto", source = "aluno.caminhoFoto")
    @Mapping(target = "dataInicio", source = "data")
    @Mapping(target = "dataFim", source = "dataFim")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "status", source = "status")
    public abstract ResBuscarSolicitacaoAgendamentoPersonalDTO toResBuscarSolicitacaoPorPersonal(
            Agendamento agendamento,
            @Context Telefone telefone
    );

    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "nome", source = "personal.nome")
    @Mapping(target = "telefone", expression = "java(telefoneMapper.buscarSolicitacoesPorAlunoTelefone(telefone))")
    @Mapping(target = "idade", expression = "java(calcularIdade(agendamento.getAluno().getDataNascimento()))")
    @Mapping(target = "foto", source = "personal.caminhoFoto")
    @Mapping(target = "dataInicio", source = "data")
    @Mapping(target = "dataFim", source = "dataFim")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "status", source = "status")
    public abstract ResBuscarSolicitacaoAgendamentoAlunoDTO toResBuscarSolicitacaoPorAluno(
            Agendamento agendamento,
            @Context Telefone telefone
    );

    @Mapping(target = "id", source = "id")
    @Mapping(target = "dataInicio", source = "data")
    @Mapping(target = "dataFim", source = "dataFim")
    @Mapping(
            target = "duracaoMinutos",
            expression = "java((int) java.time.Duration.between(agendamento.getData(), agendamento.getDataFim()).toMinutes())"
    )
    @Mapping(target = "status", source = "status")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "descricao", source = "descricao")
    @Mapping(
            target = "personal",
            expression =
                    "java(new ResDetalhesAgendamentoAlunoDTO.ResDetalhesAgendamentoPersonal(" +
                            "agendamento.getPersonal().getId(), " +
                            "agendamento.getPersonal().getNome(), " +
                            "calcularIdade(agendamento.getPersonal().getDataNascimento()), " +
                            "agendamento.getPersonal().getCaminhoFoto()" +
                            "))"
    )
    public abstract ResDetalhesAgendamentoAlunoDTO toResDetalhesAgendamentoAlunoDTO(Agendamento agendamento);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "dataInicio", source = "data")
    @Mapping(target = "dataFim", source = "dataFim")
    @Mapping(
            target = "duracaoMinutos",
            expression = "java((int) java.time.Duration.between(agendamento.getData(), agendamento.getDataFim()).toMinutes())"
    )
    @Mapping(target = "status", source = "status")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "tipoAula", source = "produtoContratado.produtoExibicao.tipoAula")
    @Mapping(target = "local", source = "endereco.complemento")
    @Mapping(target = "descricao", source = "descricao")
    @Mapping(
            target = "aluno",
            expression =
                    "java(new ResDetalhesAgendamentoPersonalDTO.ResDetalhesAgendamentoAluno(" +
                            "agendamento.getAluno().getId(), " +
                            "agendamento.getAluno().getNome(), " +
                            "calcularIdade(agendamento.getAluno().getDataNascimento()), " +
                            "agendamento.getAluno().getCaminhoFoto()" +
                            "))"
    )
    public abstract ResDetalhesAgendamentoPersonalDTO toResDetalhesAgendamentoPersonalDTO(Agendamento agendamento);

    @Mapping(source = "id", target = "agendamentoId")
    public abstract ResBuscarAgendamentoCalendarioAlunoDTO resBuscarAgendamentosParaCalendarioPorAluno(Agendamento agendamento);

    public abstract List<ResBuscarAgendamentoCalendarioAlunoDTO> resBuscarAgendamentosParaCalendarioPorAlunoList(List<Agendamento> agendamentos);

    @Mapping(source = "id", target = "agendamentoId")
    public abstract ResBuscarAgendamentoCalendarioPersonalDTO resBuscarAgendamentosParaCalendarioPorPersonal(Agendamento agendamento);

    public abstract List<ResBuscarAgendamentoCalendarioPersonalDTO> resBuscarAgendamentosParaCalendarioPorPersonalList(List<Agendamento> agendamentos);

    protected String calcularIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) return null;
        return String.valueOf(java.time.Period.between(dataNascimento, LocalDate.now()).getYears());
    }

    @Mapping(target = "alunoName", source = "aluno.nome")
    @Mapping(target = "pathImage", source = "aluno.caminhoFoto")
    public abstract ResAgendamentoByDayOfWeekDto toResAgendamentoByDayOfWeekDto(Agendamento agendamento);

}