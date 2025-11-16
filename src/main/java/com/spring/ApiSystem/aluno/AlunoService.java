package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.aluno.exception.AlunoNaoExisteException;
import com.spring.ApiSystem.aluno.mapper.AlunoMapper;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

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

        ReqCadastrarTelefoneDTO telefoneDTO = usuarioDTO.telefone();

        Telefone telefone = new Telefone();
        telefone.setPais(telefoneDTO.pais());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setNumero(telefoneDTO.numero());
        telefone.setUsuario(usuarioEntity);

        usuarioEntity.getTelefones().add(telefone);

        return alunoMapper.toDtoCadastrarAluno(alunoRepository.save(usuarioEntity));
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

    public ResAtualizarAlunoDTO atualizarUsuario(ReqAtualizarAlunoDTO dto, Usuario usuario) {

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



}
