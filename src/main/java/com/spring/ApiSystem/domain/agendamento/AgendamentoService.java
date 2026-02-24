package com.spring.ApiSystem.domain.agendamento;

import com.spring.ApiSystem.domain.agendamento.dto.request.*;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResListarConsultoriasRealizadasDto;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.events.AgendamentoEventPublisher;
import com.spring.ApiSystem.domain.agendamento.exception.*;
import com.spring.ApiSystem.domain.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.endereco.EnderecoService;
import com.spring.ApiSystem.domain.endereco.dto.response.ResCadastrarEnderecoDTO;
import com.spring.ApiSystem.domain.historicoagendamento.HistoricoAgendamentoService;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.TipoUsuario;
import com.spring.ApiSystem.domain.usuario.exception.AlunoTemAcessoApenasException;
import com.spring.ApiSystem.domain.usuario.exception.PersonalTemAcessoApenasException;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.exception.DateEndAfterBeginException;
import com.spring.ApiSystem.shared.exception.DateBeginAndEndNecessaryException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {
    private static final long HORAS_ANTECEDENCIA_MINIMA = 24;

    private final AgendamentoRepository agendamentoRepository;
    private final PersonalService personalService;
    private final ProdutoContratadoService produtoContratadoService;
    private final AlunoService alunoService;
    private final EnderecoService enderecoService;
    private final AgendamentoMapper agendamentoMapper;
    private final HistoricoAgendamentoService historicoAgendamentoService;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final AgendamentoEventPublisher agendamentoEventPublisher;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            PersonalService personalService,
            ProdutoContratadoService produtoContratadoService,
            AlunoService alunoService,
            EnderecoService enderecoService,
            AgendamentoMapper agendamentoMapper,
            HistoricoAgendamentoService historicoAgendamentoService,
            JpaUserDetailsService jpaUserDetailsService,
            AgendamentoEventPublisher agendamentoEventPublisher) {
        this.agendamentoRepository = agendamentoRepository;
        this.personalService = personalService;
        this.produtoContratadoService = produtoContratadoService;
        this.alunoService = alunoService;
        this.enderecoService = enderecoService;
        this.agendamentoMapper = agendamentoMapper;
        this.historicoAgendamentoService = historicoAgendamentoService;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.agendamentoEventPublisher = agendamentoEventPublisher;
    }

    @Transactional
    public ResCriarAgendamentoDTO criarAgendamento(ReqCadastrarAgendamentoDTO criarAgendamentoDTO) {
        validarAntecedenciaDeHorarioMarcado(criarAgendamentoDTO.data());
        LocalDateTime dataFim = calcularHorarioDeAulaPorTipoAula(criarAgendamentoDTO.data(), criarAgendamentoDTO.tipoAulaProdutoContratado());

        Usuario usuario = obterUsuarioAutenticado();
        validarSeUsuarioDoTipoAluno(usuario);

        Long produtoContratadoId = produtoContratadoService.decrementar(
                usuario.getId(),
                criarAgendamentoDTO.tipoAulaProdutoContratado()
        );

        existeAgendamentoNesteIntervalodeDeDataEHora(usuario.getId(), criarAgendamentoDTO.personalId(), criarAgendamentoDTO.data(), dataFim);

        ResCadastrarEnderecoDTO enderecoSalvo = enderecoService.cadastrarEndereco(
                criarAgendamentoDTO.novoEndereco(),
                usuario.getEmail()
        );

        Agendamento agendamento = agendamentoMapper.toEntity(criarAgendamentoDTO);
        agendamento.setEndereco(enderecoService.buscarPorId(enderecoSalvo.id()));
        agendamento.setAluno(alunoService.buscarPorId(usuario.getId()));
        agendamento.setPersonal(personalService.buscarPorId(criarAgendamentoDTO.personalId()));
        agendamento.setDescricao(criarAgendamentoDTO.descricao());
        agendamento.setProdutoContratado(produtoContratadoService.buscarPorId(produtoContratadoId));
        agendamento.setDataFim(dataFim);
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        historicoAgendamentoService.cadastrar(
                agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamentoSalvo),
                agendamentoSalvo
        );

        agendamentoEventPublisher.publishAgendamentoCreatedEvent(agendamentoSalvo);

        return agendamentoMapper.toResCriarAgendamentoDTO(agendamentoSalvo);
    }

    @Transactional
    public void reagendamento(ReqReagendarAgendamentoDTO editarAgendamentoDTO) {
        Usuario usuario = obterUsuarioAutenticado();

        validarSeAgendamentoPertenceAoUsuario(editarAgendamentoDTO.idAgendamento(), usuario.getEmail());
        validarAntecedenciaDeHorarioMarcado(editarAgendamentoDTO.data());

        Agendamento agendamento = buscarAgendamentoPorId(editarAgendamentoDTO.idAgendamento());

        LocalDateTime dataFim = calcularHorarioDeAulaPorTipoAula(
                editarAgendamentoDTO.data(),
                agendamento.getProdutoContratado().getProdutoExibicao().getTipoAula());

        existeAgendamentoNesteIntervalodeDeDataEHoraExcluindoAgendamento(
                agendamento.getAluno().getId(),
                agendamento.getPersonal().getId(),
                editarAgendamentoDTO.data(),
                dataFim,
                agendamento.getId()
        );

        agendamento.setData(editarAgendamentoDTO.data());
        agendamento.setDataFim(dataFim);
        agendamento.setDescricao(editarAgendamentoDTO.descricao());

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            agendamento.pendentePersonalAprovacao();
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            agendamento.pendenteClienteAprovacao();
        }

        ResCadastrarEnderecoDTO enderecoSalvo = enderecoService.cadastrarEndereco(
                editarAgendamentoDTO.endereco(),
                agendamento.getAluno().getEmail()
        );

        agendamento.setEndereco(enderecoService.buscarPorId(enderecoSalvo.id()));
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        historicoAgendamentoService.cadastrar(
                agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamentoSalvo),
                agendamentoSalvo
        );

        agendamentoEventPublisher.publishReagendamentoSolicitacaoEvent(agendamento, usuario);
    }

    @Transactional
    public void aprovarAgendamento(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        validarStatusParaAprovacao(agendamento, usuario);

        agendamento.aprovado();
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        historicoAgendamentoService.cadastrar(
                agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamentoSalvo),
                agendamentoSalvo
        );

        agendamentoEventPublisher.publishAgendamentoAprovadoEvent(agendamentoSalvo, usuario);
    }

    @Transactional
    public void cancelarAgendamento(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        try {
            validarAntecedenciaDeHorarioMarcado(agendamento.getData());
        } catch (AgendamentoComAtencedenciaException e) {
            throw new AgendamentoCanceladoComAtencedenciaExeception();
        }

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            agendamento.canceladoCliente();
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            agendamento.canceladoPersonal();
        }

        produtoContratadoService.incrementar(
                agendamento.getId()
        );

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        historicoAgendamentoService.cadastrar(
                agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamentoSalvo),
                agendamentoSalvo
        );

        agendamentoEventPublisher.publishAgendamentoCanceladoEvent(agendamento, usuario);
    }

    @Transactional
    public void confirmarConclusao(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        if (agendamento.getData().isAfter(LocalDateTime.now())) {
            throw new AgendamentoNaoPodeSerConcluidoException();
        }

        if (usuario.getTipo() != TipoUsuario.PERSONAL) {
            throw new PersonalTemAcessoApenasException();
        }

        if (usuario.getTipo() == TipoUsuario.PERSONAL
                && agendamento.getStatus() == AgendamentoStatus.PENDENTE_PERSONAL_CONCLUIR) {
            agendamento.concluido();
            Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
            historicoAgendamentoService.cadastrar(
                    agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamentoSalvo),
                    agendamentoSalvo
            );
        }

        agendamentoEventPublisher.publishAgendamentoConcluidoEvent(agendamento);
    }

    @Transactional
    public void registrarAusencia(ReqRegistrarAusenciaAgendamento reqAgendamento) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(reqAgendamento.idAgendamento(), usuario.getEmail());

        if (agendamento.getData().isAfter(LocalDateTime.now())) {
            throw new AgendamentoNaoPodeRegistrarAusenciaException();
        }

        if (usuario.getTipo() != TipoUsuario.PERSONAL) {
            throw new PersonalTemAcessoApenasException();
        }

        if (reqAgendamento.tipoUsuario() == TipoUsuario.PERSONAL) {
            produtoContratadoService.incrementar(agendamento.getId());
            agendamento.ausenciaPersonal();
            agendamentoEventPublisher.AusenciaRegistradaPersonalEvent(agendamento);
        } else if (reqAgendamento.tipoUsuario() == TipoUsuario.ALUNO) {
            if (reqAgendamento.descricaoCancelamento() != null) {
                agendamento.setDescricao(reqAgendamento.descricaoCancelamento());
                produtoContratadoService.incrementar(agendamento.getId());
                agendamentoEventPublisher.AusenciaRegistradaAlunoJustificadoEvent(agendamento);
            } else {
                agendamento.ausenciaCliente();
                agendamentoEventPublisher.AusenciaRegistradaAlunoEvent(agendamento);
            }
        }

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        historicoAgendamentoService.cadastrar(
                agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamentoSalvo),
                agendamentoSalvo
        );
    }

    @Transactional(readOnly = true)
    public List<?> buscarAgendamentosPorUsuario() {
        Usuario usuario = obterUsuarioAutenticado();

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            List<Agendamento> agendamentos = agendamentoRepository.buscarAgendamentosMaisProximosPorAluno(usuario.getId());
            return agendamentoMapper.toResAgendamentoAlunoOverviewDTOList(agendamentos);
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            List<Agendamento> agendamentos = agendamentoRepository.buscarAgendamentosMaisProximosPorPersonal(usuario.getId());
            return agendamentoMapper.toResAgendamentoPersonalOverviewDTOList(agendamentos);
        }
        throw new UsuarioNaoEncontradoException();
    }

    @Transactional
    public Page<?> buscarSolicitacaoPorTipoUsuarios(ReqGetAgendamentoDto dto, Pageable pageable) {
        Usuario usuario = obterUsuarioAutenticado();
        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            Page<Agendamento> agendamentosPage = agendamentoRepository.findByAlunoIdOrderByDataAsc(usuario.getId(), pageable);
            return agendamentosPage.map(agendamento -> agendamentoMapper.toResBuscarSolicitacaoPorAluno(agendamento, agendamento.getPersonal().getTelefones().getFirst()));
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            validateDateInicAndEnd(dto.dataInic(), dto.dataFim());
            Page<Agendamento> agendamentosPage = agendamentoRepository.findByPersonalIdOrderByDataAsc(
                    usuario.getId(),
                    dto.nomeDoAluno(),
                    dto.getStatusEnum(),
                    dto.getTipoAgendamentoEnum(),
                    dto.dataInic(),
                    dto.dataFim(),
                    pageable);
            return agendamentosPage.map(agendamento -> agendamentoMapper.toResBuscarSolicitacaoPorPersonal(agendamento, agendamento.getAluno().getTelefones().getFirst()));
        }
        throw new UsuarioNaoEncontradoException();
    }

    public void validateDateInicAndEnd(LocalDateTime dateBegin, LocalDateTime dateEnd) {
        if(dateBegin != null && dateEnd == null) {
            throw new DateBeginAndEndNecessaryException("dateEnd", "dateBegin");
        }
        if(dateBegin == null && dateEnd != null) {
            throw new DateBeginAndEndNecessaryException("dateBegin", "dateEnd");
        }
        if(dateBegin != null && dateEnd != null && dateBegin.isAfter(dateEnd)) {
            throw new DateEndAfterBeginException("dateEnd", "dateBegin");
        }

    }

    @Transactional
    public List<?> buscarAgendamentosParaCalendario() {
        Usuario usuario = obterUsuarioAutenticado();

        if (usuario.getTipo()== TipoUsuario.ALUNO) {
            List<Agendamento> agendamentos = agendamentoRepository.findAgendamentoByAluno_Id(usuario.getId());
            return agendamentoMapper.resBuscarAgendamentosParaCalendarioPorAlunoList(agendamentos);
        } else if (usuario.getTipo()== TipoUsuario.PERSONAL) {
            List<Agendamento> agendamentos = agendamentoRepository.findAgendamentoByPersonal_Id(usuario.getId());
            return agendamentoMapper.resBuscarAgendamentosParaCalendarioPorPersonalList(agendamentos);
        }
        throw new UsuarioNaoEncontradoException();
    }

    @Transactional(readOnly = true)
    public Object buscarDadosDoAgendamentoPorId(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            return agendamentoMapper.toResDetalhesAgendamentoAlunoDTO(agendamento);
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            return agendamentoMapper.toResDetalhesAgendamentoPersonalDTO(agendamento);
        }

        throw new UsuarioNaoEncontradoException();
    }

    public Integer buscarContagemDeAgendamentosPorPersonalStatusData(AgendamentoStatus status,
                                                                     LocalDate data) {
        Usuario usuario = obterUsuarioAutenticado();
        validarSeUsuarioDoTipoPersonal(usuario);

        return agendamentoRepository.countByPersonalIdAndStatusAndOptionalData(
                usuario.getId(),
                status,
                data);
    }

    public List<ResListarConsultoriasRealizadasDto> listarConsultoriasRealizadasMes(Integer quantidadeMeses) {
        Usuario usuario = obterUsuarioAutenticado();
        validarSeUsuarioDoTipoPersonal(usuario);

        return agendamentoRepository.listarConsultoriasRealizadasMes(usuario.getId(),
                        AgendamentoStatus.CONCLUIDO, quantidadeMeses)
                .stream()
                .map(this::converterResListarConsultoriasRealizadas)
                .toList();
    }

    /* ----------------------------- Helpers privados ----------------------------- */

    private Usuario obterUsuarioAutenticado() {
        return jpaUserDetailsService.getCurrentUser();
    }

    private Agendamento buscarAgendamentoPorId(Long agendamentoId) {
        return agendamentoRepository.findById(agendamentoId)
                .orElseThrow(AgendamentoNaoExisteException::new);
    }

    private void validarStatusParaAprovacao(Agendamento agendamento, Usuario usuario) {
        boolean situacaoPersonalAprovando = usuario.getTipo() == TipoUsuario.PERSONAL &&
                agendamento.getStatus() == AgendamentoStatus.PENDENTE_PERSONAL_APROVACAO;
        boolean situacaoAlunoAprovando = usuario.getTipo() == TipoUsuario.ALUNO &&
                agendamento.getStatus() == AgendamentoStatus.PENDENTE_CLIENTE_APROVACAO;

        if (usuario.getTipo() == TipoUsuario.PERSONAL &&
                agendamento.getStatus() == AgendamentoStatus.PENDENTE_CLIENTE_APROVACAO) {
            throw new AgendamentoPersonalNaoPodeConfirmaOAgendamento();
        } else if (usuario.getTipo() == TipoUsuario.ALUNO &&
                agendamento.getStatus() == AgendamentoStatus.PENDENTE_PERSONAL_APROVACAO) {
            throw new AgendamentoAlunoNaoPodeConfirmaOAgendamento();
        } else if (!situacaoAlunoAprovando && !situacaoPersonalAprovando) {
            throw new AgendamentoNaoPodeSerAprovadoException();
        }
    }

    private Agendamento validarSeAgendamentoPertenceAoUsuario(Long agendamentoId, String email) {
        return agendamentoRepository.buscarPorIdEEmailDoUsuario(agendamentoId, email)
                .orElseThrow(AgendamentoNaoExisteException::new);
    }

    private void validarSeUsuarioDoTipoAluno(Usuario usuario) {
        if (!usuario.getTipo().equals(TipoUsuario.ALUNO)) {
            throw new AlunoTemAcessoApenasException();
        }
    }

    private void validarSeUsuarioDoTipoPersonal(Usuario usuario) {
        if (!usuario.getTipo().equals(TipoUsuario.PERSONAL)) {
            throw new PersonalTemAcessoApenasException();
        }
    }

    private void validarAntecedenciaDeHorarioMarcado(LocalDateTime dataHora) {
        LocalDateTime dataHoraMinima = LocalDateTime.now().plusHours(HORAS_ANTECEDENCIA_MINIMA);
        if (dataHora.isBefore(dataHoraMinima)) {
            throw new AgendamentoComAtencedenciaException();
        }
    }

    private void existeAgendamentoNesteIntervalodeDeDataEHora(
            Long alunoId, Long personalId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeAgendamento = agendamentoRepository.existeConflito(
                alunoId, personalId, dataInicio, dataFim);
        if (existeAgendamento) throw new AgendamentoExistenteNestaDataHorarioException();
    }

    private void existeAgendamentoNesteIntervalodeDeDataEHoraExcluindoAgendamento(
            Long alunoId, Long personalId, LocalDateTime dataInicio, LocalDateTime dataFim, Long agendamentoId) {
        boolean existeAgendamento = agendamentoRepository.existeConflitoExcluindoAgendamento(
                alunoId, personalId, dataInicio, dataFim, agendamentoId);
        if (existeAgendamento) throw new AgendamentoExistenteNestaDataHorarioException();
    }

    private LocalDateTime calcularHorarioDeAulaPorTipoAula(LocalDateTime horarioInicio, TipoAula tipoAula) {
        if (tipoAula == TipoAula.RESIDENCIAL || tipoAula == TipoAula.PRESENCIAL) {
            return horarioInicio.plusMinutes(60);
        } else if (tipoAula == TipoAula.FUNCIONAL) {
            return horarioInicio.plusMinutes(30);
        }
        throw new AgendamentoTipoDeAulaInvalido();
    }

    private ResListarConsultoriasRealizadasDto converterResListarConsultoriasRealizadas(Object[] row) {
        return new ResListarConsultoriasRealizadasDto(
                ((Number) row[0]).intValue(),
                ((Number) row[1]).intValue(),
                ((Number) row[2]).intValue()
        );
    }
}
