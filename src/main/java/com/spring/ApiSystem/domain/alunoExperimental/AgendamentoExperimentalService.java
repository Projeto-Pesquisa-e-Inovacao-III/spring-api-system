package com.spring.ApiSystem.domain.alunoExperimental;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.AgendamentoRepository;
import com.spring.ApiSystem.domain.agendamento.AgendamentoService;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResCriarAgendamentoDTO;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoComAtencedenciaException;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoTipoDeAulaInvalido;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.alunoExperimental.dto.ReqCriarAgendamentoExperimentalRequest;
import com.spring.ApiSystem.domain.alunoExperimental.mapper.AlunoExperimentalMapper;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AgendamentoExperimentalService {

    private static final long HORAS_ANTECEDENCIA_MINIMA = 24;
    private static final String SENHA_PADRAO = "1234";
    private final PersonalService personalService;
    private final AgendamentoService agendamentoService;
    private final  AlunoService alunoService;
    private final UsuarioService usuarioService;

    public AgendamentoExperimentalService(PersonalService personalService, AgendamentoService agendamentoService, AlunoService alunoService, UsuarioService usuarioService) {
        this.personalService = personalService;
        this.agendamentoService = agendamentoService;
        this.alunoService = alunoService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public ResCriarAgendamentoDTO criarAgendamentoExperimental(ReqCriarAgendamentoExperimentalRequest request) {
        Aluno aluno = obterOuCriarAluno(request);
        validarDisponibilidadePersonal(request);
        validarAntecedenciaDeHorarioMarcado(request.agendamentoDTO().data());

        return agendamentoService.criaAgendamentoExperimental(request.agendamentoDTO(), aluno);
    }

    private Aluno obterOuCriarAluno(ReqCriarAgendamentoExperimentalRequest request) {
        String email = request.emailAluno();

        if (usuarioService.emailExiste(email)) {
            throw new EmailExistenteException();
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.nomeAluno());
        novoUsuario.setEmail(email);
        novoUsuario.setSexo(request.sexo());
        novoUsuario.setDataNascimento(request.dataNascimento());
        novoUsuario.addRole(Role.ALUNO);

        String senhaPlana = request.nomeAluno() + SENHA_PADRAO;
        usuarioService.aplicarSenhaCriptografada(novoUsuario, senhaPlana);

        Usuario usuarioSalvo = usuarioService.salvarUsuario(novoUsuario);
        return alunoService.createProfile(usuarioSalvo, new Cpf(request.cpfAluno()));
    }



    private void validarDisponibilidadePersonal(ReqCriarAgendamentoExperimentalRequest request) {
        personalService.validateDisponibilidade(
                request.agendamentoDTO().personalId(),
                DiaSemana.fromDayOfWeek(request.agendamentoDTO().data().getDayOfWeek())
        );
    }
    private void validarAntecedenciaDeHorarioMarcado(LocalDateTime dataHora) {
        LocalDateTime dataHoraMinima = LocalDateTime.now().plusHours(HORAS_ANTECEDENCIA_MINIMA);
        if (dataHora.isBefore(dataHoraMinima)) {
            throw new AgendamentoComAtencedenciaException();
        }
    }

    private LocalDateTime calcularHorarioDeAulaPorTipoAula(LocalDateTime horarioInicio, TipoAula tipoAula) {
        if (tipoAula == TipoAula.RESIDENCIAL || tipoAula == TipoAula.PRESENCIAL) {
            return horarioInicio.plusMinutes(60);
        } else if (tipoAula == TipoAula.FUNCIONAL) {
            return horarioInicio.plusMinutes(30);
        }
        throw new AgendamentoTipoDeAulaInvalido();
    }

}
