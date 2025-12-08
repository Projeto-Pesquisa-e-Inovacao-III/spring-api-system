package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.dto.response.ResListarAlunosDto;
import com.spring.ApiSystem.horariopersonal.DisponibilidadePersonalService;
import com.spring.ApiSystem.personal.dto.request.ReqAtualizarPersonalDTO;
import com.spring.ApiSystem.horariopersonal.DisponibilidadePersonalService;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarBufferDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResListarPersonaisDTO;
import com.spring.ApiSystem.personal.exception.CrefExistenteException;
import com.spring.ApiSystem.personal.exception.PersonalNaoExisteExcepetion;
import com.spring.ApiSystem.personal.mapper.PersonalMapper;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonalService {

    private final PersonalRepository personalRepository;
    private final UsuarioService usuarioService;
    private final PersonalMapper personalMapper;
    private final JpaUserDetailsService detailsService;
    private final DisponibilidadePersonalService disponibilidadeService;

    public PersonalService(PersonalRepository personalRepository, UsuarioService usuarioService, PersonalMapper personalMapper, JpaUserDetailsService detailsService, DisponibilidadePersonalService disponibilidadeService) {
        this.personalRepository = personalRepository;
        this.usuarioService = usuarioService;
        this.personalMapper = personalMapper;
        this.detailsService = detailsService;
        this.disponibilidadeService = disponibilidadeService;
    }

    public ResCadastrarPersonalDTO cadastrarUsuario(ReqCadastroPersonalDTO usuarioDTO) {
        cadastrarCrefExistente(usuarioDTO.cref());

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

    public ResAtualizarPersonalDTO atualizarUsuario(ReqAtualizarPersonalDTO dto, Personal personal) {

        usuarioService.validarEmailNaoEmUso(dto.email(), personal.getEmail());

        personalMapper.atualizarPersonalParaAtualizarPersonalDto(dto, personal);

        if (dto.telefones() != null && !dto.telefones().isEmpty()) {
            usuarioService.atualizarTelefones(personal, dto.telefones());
        }

        personalRepository.save(personal);

        return personalMapper.toDtoAtualizarPersonal(personal);
    }

    @Transactional
    public void atualizarBufferMinutos(Long personalId, Integer novoBufferMinutos) {

        Personal personal = buscarPorId(personalId);
        personal.setBufferMinutos(novoBufferMinutos);
        personalRepository.save(personal);
    }

    public ResBuscarBufferDTO buscarBuffer() {
        Personal personal = detailsService.getCurrentPersonal();
        Integer buffer = Optional.ofNullable(personal.getBufferMinutos()).orElse(15);
        return new ResBuscarBufferDTO(buffer);
    }


    public List<ResListarPersonaisDTO> listarPersonais(Pageable pageable) {
        List<Personal> personals = personalRepository.findAllAtivos(pageable);
        return personalMapper.toDtoListarPersonaisDTO(personals);
    }

    public ResBuscarPersonalPorIdDTO buscarPersonalPorId(Long id) {
        Personal personal = buscarPorId(id);
        return personalMapper.toDtoBuscarPersonalPorIdDTO(personal);
    }

    public Personal buscarPorId(Long id) {
        return personalRepository
                .findById(id)
                .orElseThrow(PersonalNaoExisteExcepetion::new);
    }

    private Personal buscarPorEmail(String email) {
        return personalRepository.findByEmail(email)
                .orElseThrow(PersonalNaoExisteExcepetion::new);
    }

    public void cadastrarCrefExistente(String cref){
        if(crefExiste(cref)){
            throw new CrefExistenteException();
        }
    }

    private boolean crefExiste(String cref){
        return personalRepository.existsByCref(cref);
    }

    private void validarCrefExistente(String cref, String crefAtual){
        if(crefExiste(cref) && !cref.equals(crefAtual)){
            throw new CrefExistenteException();
        }
    }
}
