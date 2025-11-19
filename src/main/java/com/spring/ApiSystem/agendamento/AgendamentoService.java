package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.request.ReqCadastrarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReqReagendarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReqRegistrarAusenciaAgendamentoAluno;
import com.spring.ApiSystem.agendamento.dto.request.ReqRegistrarAusenciaAgendamentoPersonal;
import com.spring.ApiSystem.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.exception.*;
import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.endereco.EnderecoService;
import com.spring.ApiSystem.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.endereco.dto.response.ResCadastrarEnderecoDTO;
import com.spring.ApiSystem.historicoagendamento.HistoricoAgendamentoService;
import com.spring.ApiSystem.personal.PersonalService;
import com.spring.ApiSystem.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import com.spring.ApiSystem.usuario.exception.AlunoNaoTemAcessoException;
import com.spring.ApiSystem.usuario.exception.PersonalNaoTemAcessoException;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.ApiSystem.usuario.Usuario;

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
    private final UsuarioService usuarioService;
    private final HistoricoAgendamentoService historicoAgendamentoService;


    public AgendamentoService(AgendamentoRepository agendamentoRepository, PersonalService personalService, ProdutoContratadoService produtoContratadoService, AlunoService alunoService, EnderecoService enderecoService, AgendamentoMapper agendamentoMapper, UsuarioService usuarioService, HistoricoAgendamentoService historicoAgendamentoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.personalService = personalService;
        this.produtoContratadoService = produtoContratadoService;
        this.alunoService = alunoService;
        this.enderecoService = enderecoService;
        this.agendamentoMapper = agendamentoMapper;
        this.usuarioService = usuarioService;
        this.historicoAgendamentoService = historicoAgendamentoService;
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

        existeAgendamentoNesteIntervalodeDeDataEHora(usuario.getId(), criarAgendamentoDTO.personalId(), criarAgendamentoDTO.data(),dataFim);

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

        return agendamentoMapper.toResCriarAgendamentoDTO(agendamentoSalvo);
    }

    @Transactional
    public void reagendamento(ReqReagendarAgendamentoDTO editarAgendamentoDTO) {
        Usuario usuario = obterUsuarioAutenticado();

        validarSeAgendamentoPertenceAoUsuario(editarAgendamentoDTO.idAgendamento(),usuario.getEmail());
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
        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void aprovarAgendamento(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        validarStatusParaAprovacao(agendamento, usuario);

        agendamento.aprovado();
        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void cancelarAgendamento(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        validarAntecedenciaDeHorarioMarcado(agendamento.getData());

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            agendamento.canceladoCliente();
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            agendamento.canceladoPersonal();
        }

        produtoContratadoService.incrementar(
                agendamento.getProdutoContratado().getId()
        );

        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void confirmarConclusao(Long agendamentoId) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(agendamentoId, usuario.getEmail());

        if (agendamento.getData().isAfter(LocalDateTime.now())) {
            throw new AgendamentoNaoPodeSerConcluidoException();
        }

        if (usuario.getTipo() == TipoUsuario.PERSONAL  && agendamento.getStatus() == AgendamentoStatus.PENDENTE_PERSONAL_CONCLUIR) {
            agendamento.concluido();
        }

        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void registrarAusenciaAlunoPorPersonal(ReqRegistrarAusenciaAgendamentoAluno reqAgendamento) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(reqAgendamento.idAgendamento(), usuario.getEmail());

        if (agendamento.getData().isAfter(LocalDateTime.now())) {
            throw new AgendamentoNaoPodeRegistrarAusenciaException();
        }

        if (usuario.getTipo() != TipoUsuario.PERSONAL) {
            throw new AlunoNaoTemAcessoException();
        }

        agendamento.setDescricao(reqAgendamento.descricaoCancelamento());
        agendamento.ausenciaCliente();

        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void registrarAusenciaPersonalPorPersonal(ReqRegistrarAusenciaAgendamentoPersonal reqAgendamento) {
        Usuario usuario = obterUsuarioAutenticado();
        Agendamento agendamento = validarSeAgendamentoPertenceAoUsuario(reqAgendamento.idAgendamento(), usuario.getEmail());

        if (agendamento.getData().isAfter(LocalDateTime.now())) {
            throw new AgendamentoNaoPodeRegistrarAusenciaException();
        }

        if (usuario.getTipo() != TipoUsuario.PERSONAL) {
            throw new UsuarioNaoEncontradoException();
        }

        agendamento.setDescricao(reqAgendamento.descricaoCancelamento());
        produtoContratadoService.incrementar(agendamento.getProdutoContratado().getId());
        agendamento.ausenciaPersonal();

        agendamentoRepository.save(agendamento);
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

    private Usuario obterUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioService.buscarUsuarioPorEmail(email);
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

        if (!situacaoAlunoAprovando && !situacaoPersonalAprovando) {
            throw new AgendamentoNaoPodeSerAprovadoException();
        }
    }

    private Agendamento validarSeAgendamentoPertenceAoUsuario(Long agendamentoId, String email) {
        return agendamentoRepository.buscarPorIdEEmailDoUsuario(agendamentoId, email)
                .orElseThrow(AgendamentoNaoExisteException::new);
    }

    private void validarSeUsuarioDoTipoAluno(Usuario usuario) {
        if (!usuario.getTipo().equals(TipoUsuario.ALUNO)) {
            throw new PersonalNaoTemAcessoException();
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
        } else if (tipoAula == TipoAula.FUNCIONAL){
            return horarioInicio.plusMinutes(30);
        }
        throw new AgendamentoTipoDeAulaInvalido();
    }
}
