package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.BuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.personal.exception.PersonalNaoExisteExcpetion;
import com.spring.ApiSystem.personal.mapper.PersonalMapper;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import org.springframework.stereotype.Service;

@Service
public class PersonalService {

    private final PersonalRepository personalRepository;
    private final UsuarioService usuarioService;
    private final PersonalMapper personalMapper;

    public PersonalService(PersonalRepository personalRepository, UsuarioService usuarioService, PersonalMapper personalMapper) {
        this.personalRepository = personalRepository;
        this.usuarioService = usuarioService;
        this.personalMapper = personalMapper;
    }

    public ResCadastrarPersonalDTO cadastrarUsuario(ReqCadastroPersonalDTO usuarioDTO) {
        usuarioService.validarEmailExistente(usuarioDTO.email());

        Personal usuarioEntity = personalMapper.toEntity(usuarioDTO);
        usuarioService.aplicarSenhaCriptografada(usuarioEntity, usuarioEntity.getSenha());

        ReqCadastrarTelefoneDTO telefoneDTO = usuarioDTO.telefone();

        Telefone telefone = new Telefone();
        telefone.setPais(telefoneDTO.pais());
        telefone.setDdd(telefoneDTO.ddd());
        telefone.setNumero(telefoneDTO.numero());
        telefone.setUsuario(usuarioEntity);

        usuarioEntity.getTelefones().add(telefone);


        return personalMapper.toDtoCadastrarPersonal(personalRepository.save(usuarioEntity));
    }



    public BuscarPersonalPorIdDTO buscarPersonalPorId(Long id) {
    Personal  personal = findById(id);

        return new BuscarPersonalPorIdDTO(
                personal.getId(),
                personal.getNome(),
                personal.getSexo(),
                personal.getDataNascimento(),
                personal.getEmail(),
                personal.getCref(),
                personal.isAtivo()
        );
    }

    public Personal findById(Long id) {

        return personalRepository
                .findById(id)
                .orElseThrow(PersonalNaoExisteExcpetion::new);
    }
}
