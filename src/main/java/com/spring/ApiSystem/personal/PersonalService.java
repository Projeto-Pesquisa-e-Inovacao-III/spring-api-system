package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.horariopersonal.DisponibilidadePersonalService;
import com.spring.ApiSystem.personal.dto.request.ReqAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.personal.exception.PersonalNaoExisteExcpetion;
import com.spring.ApiSystem.personal.mapper.PersonalMapper;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonalService {

    private final PersonalRepository personalRepository;
    private final UsuarioService usuarioService;
    private final PersonalMapper personalMapper;
    private final DisponibilidadePersonalService disponibilidadeService;

    public PersonalService(PersonalRepository personalRepository, UsuarioService usuarioService, PersonalMapper personalMapper, DisponibilidadePersonalService disponibilidadeService) {
        this.personalRepository = personalRepository;
        this.usuarioService = usuarioService;
        this.personalMapper = personalMapper;
        this.disponibilidadeService = disponibilidadeService;
    }

    public ResCadastrarPersonalDTO cadastrarUsuario(ReqCadastroPersonalDTO usuarioDTO) {
        usuarioService.validarEmailExistente(usuarioDTO.email());

        Personal usuarioEntity = personalMapper.toEntity(usuarioDTO);
        usuarioService.aplicarSenhaCriptografada(usuarioEntity, usuarioEntity.getSenha());

        Integer bufferFinal = Optional.ofNullable(usuarioDTO.bufferMinutos()).orElse(15);
        usuarioEntity.setBufferMinutos(bufferFinal);


        ReqCadastrarTelefoneDTO telefoneDTO = usuarioDTO.telefone();

        Telefone telefone = new Telefone();
        telefone.setPais(telefoneDTO.pais());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setNumero(telefoneDTO.numero());
        telefone.setUsuario(usuarioEntity);

        usuarioEntity.getTelefones().add(telefone);

        Personal personalSalvo = personalRepository.save(usuarioEntity);

        disponibilidadeService.criarDisponibilidadePadrao(personalSalvo.getId());

        return personalMapper.toDtoCadastrarPersonal(personalRepository.save(usuarioEntity));
    }

    @Transactional
    public void atualizarBufferMinutos(Long personalId, Integer novoBufferMinutos) {

        Personal personal = findById(personalId);
        personal.setBufferMinutos(novoBufferMinutos);
        personalRepository.save(personal);
    }

    public ResBuscarPersonalPorIdDTO buscarPersonalPorId(Long id) {
    Personal  personal = findById(id);
    return personalMapper.toDtoBuscarPersonalPorIdDTO(personal);
    }

    public Personal findById(Long id) {

        return personalRepository
                .findById(id)
                .orElseThrow(PersonalNaoExisteExcpetion::new);
    }

    public Personal buscarPorId(Long id) {
        return personalRepository
                .findById(id)
                .orElseThrow(PersonalNaoExisteExcpetion::new);
    }

    public ResAtualizarPersonalDTO atualizarUsuario(ReqAtualizarPersonalDTO dto, Usuario usuario) {

        usuarioService.validarEmailNaoEmUso(dto.email(), usuario.getEmail());

        usuarioService.validarSenhaAtual(dto.senha(), usuario);

        Personal personal = buscarPorId(usuario.getId());

        personalMapper.atualizarPersonalParaAtualizarPersonalDto(dto, personal);

        if (dto.telefones() != null && !dto.telefones().isEmpty()) {
            usuarioService.atualizarTelefones(personal, dto.telefones());
        }

        if (dto.senhaNova() != null) {
            usuarioService.aplicarSenhaCriptografada(personal, dto.senhaNova());
        }

        personalRepository.save(personal);

        return personalMapper.toDtoAtualizarPersonal(personal);
    }

}
