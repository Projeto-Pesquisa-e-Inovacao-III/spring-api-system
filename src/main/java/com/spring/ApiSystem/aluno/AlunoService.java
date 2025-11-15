package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.aluno.exception.AlunoNaoExisteExcpetion;
import com.spring.ApiSystem.aluno.mapper.AlunoMapper;
import com.spring.ApiSystem.usuario.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioService usuarioService;
    private final AlunoMapper alunoMapper;

    public AlunoService(AlunoRepository alunoRepository, UsuarioService usuarioService, AlunoMapper alunoMapper) {
        this.alunoRepository = alunoRepository;
        this.usuarioService = usuarioService;
        this.alunoMapper = alunoMapper;
    }

    public ResCadastrarAlunoDTO cadastrarUsuario(ReqCadastroAlunoDTO usuarioDTO) {
        usuarioService.validarEmailExistente(usuarioDTO.email());

        Aluno usuarioEntity = alunoMapper.toEntityAluno(usuarioDTO);
        usuarioService.aplicarSenhaCriptografada(usuarioEntity, usuarioEntity.getSenha());

        return alunoMapper.toDtoCadastrarAluno(alunoRepository.save(usuarioEntity));
    }


    public BuscarAlunoPorIdDTO buscarAlunoPorId(Long id) {
        Aluno aluno = buscarPorId(id);

        return new BuscarAlunoPorIdDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getSexo(),
                aluno.getDataNascimento(),
                aluno.getEmail(),
                aluno.getCpf(),
                aluno.isAtivo()
        );
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository
                .findById(id)
                .orElseThrow(AlunoNaoExisteExcpetion::new);
    }

}
