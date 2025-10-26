package com.spring.ApiSystem.service;

import com.spring.ApiSystem.mapper.AgendamentoMapper;
import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.model.Aluno;
import com.spring.ApiSystem.model.Personal;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.repository.AgendamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final UsuarioService usuarioService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, AgendamentoMapper agendamentoMapper, UsuarioService usuarioService) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoMapper = agendamentoMapper;
        this.usuarioService = usuarioService;
    }

    public Agendamento save(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    public Page<?> pegarTodosAgendamentosDesseUsuario(String email, int page, int size){
        Usuario usuario = usuarioService.pegarUsuarioPeloEmail(email);

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

}
