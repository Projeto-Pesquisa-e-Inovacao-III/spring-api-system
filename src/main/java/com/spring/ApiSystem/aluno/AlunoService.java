package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResAlunosPagantesDTO;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.aluno.exception.AlunoNaoExisteException;
import com.spring.ApiSystem.aluno.exception.AlunoPersistenciaException;
import com.spring.ApiSystem.aluno.dto.response.ResListarAlunosDto;
import com.spring.ApiSystem.aluno.exception.CpfExistenteException;
import com.spring.ApiSystem.aluno.mapper.AlunoMapper;
import com.spring.ApiSystem.eventos.aluno.AlunoEventPublisher;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UsuarioService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioService usuarioService;
    private final AlunoMapper alunoMapper;
    private final AlunoEventPublisher alunoEventPublisher;

    public AlunoService(AlunoRepository alunoRepository, UsuarioService usuarioService, AlunoMapper alunoMapper, AlunoEventPublisher alunoEventPublisher) {
        this.alunoRepository = alunoRepository;
        this.usuarioService = usuarioService;
        this.alunoMapper = alunoMapper;
        this.alunoEventPublisher = alunoEventPublisher;
    }

    public ResCadastrarAlunoDTO cadastrarUsuario(ReqCadastroAlunoDTO usuarioDTO) {
        cadastrarCpfExistente(usuarioDTO.cpf());

        usuarioService.validarEmailExistente(usuarioDTO.email());

        Aluno usuarioEntity = alunoMapper.toEntityAluno(usuarioDTO);
        usuarioService.aplicarSenhaCriptografada(usuarioEntity, usuarioEntity.getSenha());

        ReqCadastrarTelefoneDTO telefoneDTO = usuarioDTO.telefone();

        Telefone telefone = new Telefone();
        telefone.setPais(telefoneDTO.pais());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setNumero(telefoneDTO.numero());
        telefone.setUsuario(usuarioEntity);

        usuarioEntity.getTelefones().add(telefone);
        try {
            Aluno alunoSalvo = alunoRepository.save(usuarioEntity);
            if (alunoSalvo.getId() == null) {
                throw new AlunoPersistenciaException("Falha ao salvar aluno: ID não gerado.");
            }

            alunoEventPublisher.publishAlunoCreatedEvent(alunoSalvo);
            return alunoMapper.toDtoCadastrarAluno(alunoSalvo);
        } catch (DataIntegrityViolationException e) {
            throw new AlunoPersistenciaException("Violação de integridade ao salvar aluno.", e);
        } catch (DataAccessException e) {
            throw new AlunoPersistenciaException("Erro de acesso a dados ao salvar aluno.", e);
        }
    }

    public List<ResListarAlunosDto> listarAlunos(Pageable pageable) {
        List<Aluno> alunos = alunoRepository.findAllAtivos(pageable);
        return alunoMapper.toResListarAlunosDto(alunos);
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

    public void cadastrarCpfExistente(String cpf){
        if (cpfExiste(cpf)) {
            throw new CpfExistenteException();
        }
    }

    public boolean cpfExiste(String cpf){
        return alunoRepository.existsByCpf(cpf);
    }

    public void validarCpfExistente(String cpf, String cpfAtual){
        if (cpfExiste(cpf) && !cpf.equals(cpfAtual)) {
            throw new CpfExistenteException();
        }
    }

    public ResAtualizarAlunoDTO atualizarUsuario(ReqAtualizarAlunoDTO dto, Aluno usuario) {
        validarCpfExistente(dto.cpf(), usuario.getCpf());

        usuarioService.validarEmailNaoEmUso(dto.email(), usuario.getEmail());

        usuarioService.validarSenhaAtual(dto.senha(), usuario);

        Aluno aluno = buscarPorId(usuario.getId());

        alunoMapper.atualizarAlunoParaAtualizarAlunoDto(dto, aluno);

        if (dto.telefones() != null && !dto.telefones().isEmpty()) {
            usuarioService.atualizarTelefones(aluno, dto.telefones());
        }

        if (dto.senhaNova() != null) {
            usuarioService.aplicarSenhaCriptografada(aluno, dto.senhaNova());
        }

        alunoRepository.save(aluno);

        return alunoMapper.toDtoAtualizarAluno(aluno);
    }

    public ResAlunosPagantesDTO contarAlunosComPlanosAtivos() {
        Integer quantidade = alunoRepository.countAlunosComPlanosAtivos();
        return new ResAlunosPagantesDTO(quantidade);
    }
}
