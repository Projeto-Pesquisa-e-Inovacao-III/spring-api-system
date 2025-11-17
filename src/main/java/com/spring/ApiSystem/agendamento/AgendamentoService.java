package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.request.ReqCriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.exception.AgendamentoComAtencedenciaException;
import com.spring.ApiSystem.agendamento.exception.AgendamentoExistenteNestaDataHorarioException;
import com.spring.ApiSystem.agendamento.exception.AgendamentoTipoDeAulaInvalido;
import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.endereco.EnderecoService;
import com.spring.ApiSystem.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.endereco.dto.response.ResCadastrarEnderecoDTO;
import com.spring.ApiSystem.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.personal.PersonalService;
import com.spring.ApiSystem.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import com.spring.ApiSystem.usuario.exception.PersonalNaoTemAcessoException;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.ApiSystem.usuario.Usuario;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PersonalService personalService;
    private final ProdutoContratadoService produtoContratadoService;
    private final AlunoService alunoService;
    private final EnderecoService enderecoService;
    private final AgendamentoMapper agendamentoMapper;
    private final UsuarioService usuarioService;
    private final EnderecoMapper enderecoMapper;
    private final ProdutoContratadoMapper produtoContratadoMapper;


    public AgendamentoService(AgendamentoRepository agendamentoRepository, PersonalService personalService, ProdutoContratadoService produtoContratadoService, AlunoService alunoService, EnderecoService enderecoService, AgendamentoMapper agendamentoMapper, UsuarioService usuarioService, EnderecoMapper enderecoMapper, ProdutoContratadoMapper produtoContratadoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.personalService = personalService;
        this.produtoContratadoService = produtoContratadoService;
        this.alunoService = alunoService;
        this.enderecoService = enderecoService;
        this.agendamentoMapper = agendamentoMapper;
        this.usuarioService = usuarioService;
        this.enderecoMapper = enderecoMapper;
        this.produtoContratadoMapper = produtoContratadoMapper;
    }

    @Transactional
    public ResCriarAgendamentoDTO criarAgendamento(ReqCriarAgendamentoDTO criarAgendamentoDTO, String email) {
        validarAntecedenciaDeHorarioMarcado(criarAgendamentoDTO.data());
        LocalDateTime dataFim = calcularHorarioDeAulaPorTipoAula(criarAgendamentoDTO.data(), criarAgendamentoDTO.tipoAulaProdutoContratado());
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);

        validarSeUsuarioDoTipoAluno(email);
        Long produtoContratadoId = produtoContratadoService.decrementar(
                usuario.getId(),
                criarAgendamentoDTO.tipoAulaProdutoContratado()
        );

        existeAgendamentoNestaDataEHora(usuario.getId(), criarAgendamentoDTO.personalId(), criarAgendamentoDTO.data(),dataFim);

        ResCadastrarEnderecoDTO enderecoSalvo = enderecoService.cadastrarEndereco(
                criarAgendamentoDTO.novoEndereco(),
                email
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


    @Transactional(readOnly = true)
    public List<?> buscarAgendamentosPorUsuario(String email) {
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            List<Agendamento> agendamentos = agendamentoRepository.buscarAgendamentosMaisProximosPorAluno(usuario.getId());
            return agendamentoMapper.toResAgendamentoAlunoOverviewDTOList(agendamentos);
        } else if (usuario.getTipo() == TipoUsuario.PERSONAL) {
            List<Agendamento> agendamentos = agendamentoRepository.buscarAgendamentosMaisProximosPorPersonal(usuario.getId());
            return agendamentoMapper.toResAgendamentoPersonalOverviewDTOList(agendamentos);
        }
        throw new UsuarioNaoEncontradoException();
    }

    public void editarAgendamento( ReqCriarAgendamentoDTO editarAgendamentoDTO, String email) {

    }

    private void existeAgendamentoNestaDataEHora(Long alunoId, Long personalId, LocalDateTime dataInico, LocalDateTime dataFim) {
       boolean existeAgenmento =  agendamentoRepository.existsByAlunoIdAndPersonalIdAndDataBetween(alunoId, personalId,dataInico,dataFim);
        if (existeAgenmento) throw  new AgendamentoExistenteNestaDataHorarioException();
    }

    private void validarSeUsuarioDoTipoAluno(String email) {
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        if (!usuario.getTipo().equals(TipoUsuario.ALUNO)) {
            throw new PersonalNaoTemAcessoException();
        }
    }

    private void validarAntecedenciaDeHorarioMarcado(LocalDateTime dataHora) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataHoraMinima = agora.plusHours(24);

        if (dataHora.isBefore(dataHoraMinima)) {
            throw new AgendamentoComAtencedenciaException();
        }
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
