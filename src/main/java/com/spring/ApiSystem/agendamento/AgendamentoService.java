package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.agendamento.dto.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.response.BuscarAgendamentoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReagendarAgendamentoDTO;
import com.spring.ApiSystem.endereco.EnderecoService;
import com.spring.ApiSystem.endereco.dto.response.ResCadastrarEnderecoDTO;
import com.spring.ApiSystem.endereco.dto.response.ResEnderecoDTO;
import com.spring.ApiSystem.agendamento.exception.AgendamentoNaoExistePorIdException;
import com.spring.ApiSystem.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.shared.exception.DataNoPassadoException;
import com.spring.ApiSystem.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.agendamento.enums.Situacao;
import com.spring.ApiSystem.personal.PersonalService;
import com.spring.ApiSystem.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.usuario.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    public AgendamentoService(AgendamentoRepository agendamentoRepository, PersonalService personalService, ProdutoContratadoService produtoContratadoService, AlunoService alunoService, EnderecoService enderecoService, AgendamentoMapper agendamentoMapper, UsuarioService usuarioService) {
        this.agendamentoRepository = agendamentoRepository;
        this.personalService = personalService;
        this.produtoContratadoService = produtoContratadoService;
        this.alunoService = alunoService;
        this.enderecoService = enderecoService;
        this.agendamentoMapper = agendamentoMapper;
        this.usuarioService = usuarioService;
    }

    public Page<?> pegarTodosAgendamentosDesseUsuario(String email, int page, int size){
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);

        Pageable pageable = PageRequest.of(page, size);

        List<?> returnDTO = new ArrayList<>();

        if(usuario.getTipo().equals("Aluno")){
            Page<Agendamento> agendamentos = agendamentoRepository
                    .findByAlunoOrderByDataAsc((Aluno) usuario, pageable);

            return agendamentos.map(agendamentoMapper::toListarAgendamentoAlunoDto);
        } else if (usuario.getTipo().equals("Personal")) {
            Page<Agendamento> agendamentos = agendamentoRepository
                    .findByPersonalOrderByDataAsc((Personal) usuario, pageable);

            return agendamentos.map(agendamentoMapper::toListarAgendamentoPersonalDto);
        }

        return Page.empty();
    }

//    @Transactional
//    public Agendamento criar(CriarAgendamentoDTO dto) {
//
//        if (dto.data().isBefore(LocalDateTime.now())) {
//            throw new DataNoPassadoException();
//        }
//
//        BuscarAlunoPorIdDTO aluno = alunoService.buscarAlunoPorId(dto.alunoId());
//        personalService.buscarPersonalPorId((dto.personalId()));
//        produtoContratadoService.buscarPorIdProdutoContratado(Math.toIntExact(dto.produtoContratadoId()));
//
//        Endereco endereco;
//        if (dto.enderecoExistenteId() != null) {
//            endereco = enderecoService.buscarPorId(dto.enderecoExistenteId());
//
//        } else {
//            ResCadastrarEnderecoDTO resEndereco = enderecoService.cadastrarEndereco(dto.novoEndereco(), aluno.email());
//            endereco = enderecoService.buscarPorId(resEndereco.id());
//        }
//
//        Agendamento agendamento = agendamentoMapper.toEntity(dto);
//
//        agendamento.setEndereco(endereco);
//        agendamento.setSituacao(Situacao.PENDENTE_PERSONAL);
//
//        return agendamentoRepository.save(agendamento);
//    }
//
//    public Agendamento buscarPorId(Long id) {
//        return agendamentoRepository
//                .findById(id)
//                .orElseThrow(AgendamentoNaoExistePorIdException::new);
//    }

//    @Transactional
//    public Agendamento reagendar(Long agendamentoId, ReagendarAgendamentoDTO dto) {
//        Agendamento agendamento = buscarPorId(agendamentoId);
//
//        if (dto.novaData().isBefore(LocalDateTime.now())) {
//            throw new DataNoPassadoException();
//        }
//
//        agendamento.setData(dto.novaData());
//
//        if (dto.novaDescricao() != null && !dto.novaDescricao().isBlank()) {
//            agendamento.setDescricao(dto.novaDescricao());
//        }
//
//        if (dto.enderecoExistenteId() != null || dto.novoEndereco() != null) {
//            Endereco novoEndereco;
//            if (dto.enderecoExistenteId() != null) {
//                novoEndereco = enderecoService.buscarPorId(dto.enderecoExistenteId());
//            } else {
//                ResCadastrarEnderecoDTO resEndereco = enderecoService.cadastrarEndereco(
//                        dto.novoEndereco(),
//                        agendamento.getAluno().getEmail()
//                );
//                novoEndereco = enderecoService.buscarPorId(resEndereco.id());
//            }
//            agendamento.setEndereco(novoEndereco);
//        }
//
//        if (dto.tipoUsuario().equalsIgnoreCase("PERSONAL")) {
//            agendamento.pendenteCliente();
//        } else if (dto.tipoUsuario().equalsIgnoreCase("ALUNO")) {
//            agendamento.pendentePersonal();
//        } else {
//            throw new IllegalArgumentException("Tipo de usuário inválido: " + dto.tipoUsuario());
//        }
//
//        return agendamentoRepository.save(agendamento);
//    }

//    @Transactional
//    public Agendamento aceitaAgendamento(Long id) {
//        Agendamento agendamento = buscarPorId(id);
//        agendamento.aceitar();
//        return agendamentoRepository.save(agendamento);
//    }

//    @Transactional
//    public Agendamento recusaAgendamento(Long id) {
//        Agendamento agendamento = buscarPorId(id);
//        agendamento.recusado();
//        return agendamentoRepository.save(agendamento);
//    }
//
//    @Transactional(readOnly = true)
//    public BuscarAgendamentoPorIdDTO buscarAgendamentoPorId(Long id) {
//        Agendamento agendamento = buscarPorId(id);
//        return agendamentoMapper.toDTO(agendamento);
//    }

}
