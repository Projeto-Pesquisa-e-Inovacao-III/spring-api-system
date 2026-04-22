package com.spring.ApiSystem.domain.aluno;

import com.spring.ApiSystem.domain.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResAlunosPagantesDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.domain.aluno.exception.AlunoNaoExisteException;
import com.spring.ApiSystem.domain.aluno.exception.AlunoPersistenciaException;
import com.spring.ApiSystem.domain.aluno.dto.response.ResListarAlunosDto;
import com.spring.ApiSystem.domain.aluno.exception.CpfExistenteException;
import com.spring.ApiSystem.domain.aluno.mapper.AlunoMapper;
import com.spring.ApiSystem.domain.aluno.events.AlunoEventPublisher;
import com.spring.ApiSystem.domain.aluno.mapper.CpfMapper;
import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.shared.config.filter.FilterService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioService usuarioService;
    private final AlunoMapper alunoMapper;
    private final AlunoEventPublisher alunoEventPublisher;
    private final CpfMapper cpfMapper;
    private final FilterService filterService;

    public AlunoService(AlunoRepository alunoRepository, UsuarioService usuarioService, AlunoMapper alunoMapper, AlunoEventPublisher alunoEventPublisher, CpfMapper cpfMapper, FilterService filterService) {
        this.alunoRepository = alunoRepository;
        this.usuarioService = usuarioService;
        this.alunoMapper = alunoMapper;
        this.alunoEventPublisher = alunoEventPublisher;
        this.cpfMapper = cpfMapper;
        this.filterService = filterService;
    }

    @Transactional
    public ResCadastrarAlunoDTO cadastrarAluno(ReqCadastroAlunoDTO usuarioDTO, HttpServletResponse response) {
        validarCpfUnico(cpfMapper.toCpf(usuarioDTO.cpf()));
        usuarioService.validarEmailExistente(usuarioDTO.email());

        String rawPassword = usuarioDTO.senha();

        Aluno usuarioEntity = alunoMapper.toEntityAluno(usuarioDTO);
        usuarioEntity.getUsuario().addRole(Role.ALUNO);
        usuarioService.aplicarSenhaCriptografada(usuarioEntity.getUsuario(), rawPassword);

        ReqCadastrarTelefoneDTO telefoneDTO = usuarioDTO.telefone();


        Telefone telefone = criarTelefone(usuarioDTO.telefone(), usuarioEntity);
        usuarioEntity.getTelefones().add(telefone);


        try {
            Aluno alunoSalvo = alunoRepository.save(usuarioEntity);
            if (alunoSalvo.getId() == null) {
                throw new AlunoPersistenciaException("Falha ao salvar aluno: ID não gerado.");
            }

            alunoEventPublisher.publishAlunoCreatedEvent(alunoSalvo);
            Boolean isUsuarioEncontrado = usuarioService.loginUsuario(alunoSalvo.getEmail(), rawPassword);

            if (isUsuarioEncontrado) {
                filterService.gerarCookie(response, alunoSalvo.getEmail());
            }
            return alunoMapper.toDtoCadastrarAluno(alunoSalvo);
        } catch (DataIntegrityViolationException e) {
            throw new AlunoPersistenciaException("Violação de integridade ao salvar aluno.", e);
        } catch (DataAccessException e) {
            throw new AlunoPersistenciaException("Erro de acesso a dados ao salvar aluno.", e);
        }
    }

    public Aluno registrarAnamnese(Aluno aluno, Anamnese anamnese) {
        aluno.setAnamnese(anamnese);
        aluno.setAtivoAnamnese(true);
        return alunoRepository.save(aluno);
    }

    public ResAtualizarAlunoDTO atualizarUsuario(ReqAtualizarAlunoDTO dto, Aluno usuario) {

        usuarioService.validarEmailNaoEmUso(dto.email(), usuario.getEmail());


        Aluno aluno = buscarPorId(usuario.getId());

        alunoMapper.atualizarAlunoParaAtualizarAlunoDto(dto, aluno);

        if (dto.telefones() != null && !dto.telefones().isEmpty()) {
            usuarioService.atualizarTelefones(aluno, dto.telefones());
        }

        alunoRepository.save(aluno);

        return alunoMapper.toDtoAtualizarAluno(aluno);
    }

    public Page<ResListarAlunosDto> listarAlunos(Pageable pageable, String nome) {
        if(nome != null && !nome.isBlank()){
            pageable = PageRequest.of(0, pageable.getPageSize());
        }

        Page<Aluno> alunos = alunoRepository.findAllAtivosContainingNome(pageable, nome);

        return alunos.map(alunoMapper::toResListarAlunosDto);
    }

    public ResBuscarAlunoPorIdDTO buscarAlunoPorId(Long id) {
        Aluno aluno = buscarPorId(id);
        return alunoMapper.toDtoBuscarAlunoPorId(aluno);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository
                .findById(id)
                .orElseThrow(AlunoNaoExisteException::new);
    }

    public Aluno buscarPorEmail(String email){
        return alunoRepository.findByEmail(email)
                .orElseThrow(AlunoNaoExisteException::new);
    }

    public ResAlunosPagantesDTO contarAlunosComPlanosAtivos() {
        Integer quantidade = alunoRepository.countAlunosComPlanosAtivos(TipoProduto.PACOTE);
        return new ResAlunosPagantesDTO(quantidade);
    }

    private void validarCpfUnico(Cpf cpf){
        if (cpfExiste(cpf)) {
            throw new CpfExistenteException();
        }
    }

    private boolean cpfExiste(Cpf cpf){
        return alunoRepository.existsByCpf(cpf);
    }

    private Telefone criarTelefone(ReqCadastrarTelefoneDTO telefoneDTO, Aluno usuario) {
        Telefone telefone = new Telefone();
        telefone.setPais(telefoneDTO.pais());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setNumero(telefoneDTO.numero());
        telefone.setUsuario(usuario.getUsuario());
        return telefone;
    }

    public Aluno enableProfile(Long usuarioId){
        Aluno aluno = buscarPorId(usuarioId);
        aluno.setProfileAtivo(false);
        return alunoRepository.save(aluno);
    }

    public Aluno disableProfile(Long usuarioId){
        Aluno aluno = buscarPorId(usuarioId);
        aluno.setProfileAtivo(true);
        return alunoRepository.save(aluno);
    }

    public Aluno createProfile(Usuario usuario, Cpf cpf){
        cadastrarCpfExistente(cpf);
        return alunoRepository.save(new Aluno(null, usuario, cpf, false, null, true));
    }

}
