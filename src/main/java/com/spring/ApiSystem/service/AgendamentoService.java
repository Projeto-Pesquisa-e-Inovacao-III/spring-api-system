package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.agendamento.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.dto.agendamento.response.BuscarAgendamentoPorIdDTO;
import com.spring.ApiSystem.dto.aluno.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.dto.reagendamento.request.ReagendarAgendamentoDTO;
import com.spring.ApiSystem.exception.AgendamentoNaoExistePorIdException;
import com.spring.ApiSystem.exception.DataNoPassadoException;
import com.spring.ApiSystem.mapper.AgendamentoMapper;
import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.enums.Situacao;
import com.spring.ApiSystem.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PersonalService personalService;
    private final ProdutoContratadoService produtoContratadoService;
    private final AlunoService alunoService;
    private final EnderecoService enderecoService;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PersonalService personalService, ProdutoContratadoService produtoContratadoService, AlunoService alunoService, EnderecoService enderecoService, AgendamentoMapper agendamentoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.personalService = personalService;
        this.produtoContratadoService = produtoContratadoService;
        this.alunoService = alunoService;
        this.enderecoService = enderecoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @Transactional
    public Agendamento criar(CriarAgendamentoDTO dto) {

        if (dto.data().isBefore(LocalDateTime.now())) {
            throw new DataNoPassadoException();
        }

        BuscarAlunoPorIdDTO aluno = alunoService.buscarAlunoPorId(Math.toIntExact(dto.alunoId()));
        personalService.buscarPersonalPorId(Math.toIntExact(dto.personalId()));
        produtoContratadoService.buscarPorIdProdutoContratado(Math.toIntExact(dto.produtoContratadoId()));

        Endereco endereco;
        if (dto.enderecoExistenteId() != null) {
            endereco = enderecoService.findById(dto.enderecoExistenteId());
        } else {
            ResEnderecoDTO resEndereco = enderecoService.cadastrarEndereco(dto.novoEndereco(), aluno.email());
            endereco = enderecoService.findById(resEndereco.id());
        }

        Agendamento agendamento = agendamentoMapper.toEntity(dto);

        agendamento.setEndereco(endereco);
        agendamento.setSituacao(Situacao.PENDENTE_PERSONAL);

        return agendamentoRepository.save(agendamento);
    }

    public Agendamento findById(Long id) {
        return agendamentoRepository
                .findById(id)
                .orElseThrow(AgendamentoNaoExistePorIdException::new);
    }

    @Transactional
    public Agendamento reagendar(Long agendamentoId, ReagendarAgendamentoDTO dto) {
        Agendamento agendamento = findById(agendamentoId);

        if (dto.novaData().isBefore(LocalDateTime.now())) {
            throw new DataNoPassadoException();
        }

        agendamento.setData(dto.novaData());

        if (dto.novaDescricao() != null && !dto.novaDescricao().isBlank()) {
            agendamento.setDescricao(dto.novaDescricao());
        }

        if (dto.enderecoExistenteId() != null || dto.novoEndereco() != null) {
            Endereco novoEndereco;
            if (dto.enderecoExistenteId() != null) {
                novoEndereco = enderecoService.findById(dto.enderecoExistenteId());
            } else {
                ResEnderecoDTO resEndereco = enderecoService.cadastrarEndereco(
                        dto.novoEndereco(),
                        agendamento.getAluno().getEmail()
                );
                novoEndereco = enderecoService.findById(resEndereco.id());
            }
            agendamento.setEndereco(novoEndereco);
        }

        if (dto.tipoUsuario().equalsIgnoreCase("PERSONAL")) {
            agendamento.pendenteCliente();
        } else if (dto.tipoUsuario().equalsIgnoreCase("ALUNO")) {
            agendamento.pendentePersonal();
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + dto.tipoUsuario());
        }

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento aceitaAgendamento(Long id) {
        Agendamento agendamento = findById(id);
        agendamento.aceitar();
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento recusaAgendamento(Long id) {
        Agendamento agendamento = findById(id);
        agendamento.recusado();
        return agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public BuscarAgendamentoPorIdDTO buscarAgendamentoPorId(Long id) {
        Agendamento agendamento = findById(id);
        return agendamentoMapper.toDTO(agendamento);
    }

}
