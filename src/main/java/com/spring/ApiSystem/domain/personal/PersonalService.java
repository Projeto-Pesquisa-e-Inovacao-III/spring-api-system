package com.spring.ApiSystem.domain.personal;

import com.spring.ApiSystem.domain.disponibilidade.DisponibilidadePersonal;
import com.spring.ApiSystem.domain.disponibilidade.DisponibilidadePersonalService;
import com.spring.ApiSystem.domain.disponibilidade.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResHorarioDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResSlotDisponivelDTO;
import com.spring.ApiSystem.domain.disponibilidade.enums.TipoHorario;
import com.spring.ApiSystem.domain.personal.dto.request.ReqAtualizarPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResAtualizarPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResBuscarBufferDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResListarPersonaisDTO;
import com.spring.ApiSystem.domain.personal.exception.CrefExistenteException;
import com.spring.ApiSystem.domain.personal.exception.PersonalNaoExisteExcepetion;
import com.spring.ApiSystem.domain.personal.mapper.PersonalMapper;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    public Page<ResListarPersonaisDTO> listarPersonais(Pageable pageable) {
        Page<Personal> personals = personalRepository.findAllAtivos(pageable);
        return personals.map(personalMapper::toResListarPersonaisDTO);
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

    public List<ResSlotDisponivelDTO> consultarDisponibilidade(Long personalId, LocalDate data, TipoAula tipoAula) {
        Personal personal = buscarPorId(personalId);
        return disponibilidadeService.obterHorariosDisponiveis(personal, data, tipoAula);
    }

    public ResHorarioDTO atualizarHorarioDisponibilidade(Long horarioId, @Valid ReqHorarioDTO request) {
        Personal personal = detailsService.getCurrentPersonal();

        return disponibilidadeService.atualizarHorarios(personal, horarioId, request);
    }

    public List<DisponibilidadePersonal> pegarCronogramaDoPersonal() {
        Personal personal = detailsService.getCurrentPersonal();
        return disponibilidadeService.pegarCronograma(personal);
    }

    public List<DisponibilidadePersonal> findByPersonalIdAndDiaSemana(Long id, DiaSemana diaSemana){
        return disponibilidadeService.findByPersonalIdAndDiaSemana(id, diaSemana);
    }

    public void validateDisponibilidade(Long idPersonal, DiaSemana diaSemana){
        disponibilidadeService.validateDisponibilidade(idPersonal, diaSemana);
    }

    public List<DisponibilidadePersonal> changeActivation(DiaSemana diaSemana){
        return disponibilidadeService.changeActivation(diaSemana);
    }

    public List<DisponibilidadePersonal> findDiaSemanaByPersonalAndTipo(TipoHorario tipoHorario){
        Personal personal = detailsService.getCurrentPersonal();

        return disponibilidadeService.findByPersonalAndTipo(personal.getId(), tipoHorario);
    }
}
