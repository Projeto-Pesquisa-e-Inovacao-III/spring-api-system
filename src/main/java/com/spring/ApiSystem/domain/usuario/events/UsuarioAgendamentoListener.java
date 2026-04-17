package com.spring.ApiSystem.domain.usuario.events;

import com.spring.ApiSystem.domain.agendamento.AgendamentoRepository;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UsuarioAgendamentoListener implements UsuarioListener {

    private final AgendamentoRepository agendamentoRepository;

    public UsuarioAgendamentoListener(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Override
    @Transactional
    public void onUsuarioRemovido(Usuario usuario) {
        if (usuario == null || usuario.getRoles().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        long id = usuario.getId();
        String tipo;

        AgendamentoStatus novoStatus;
        if (usuario.isAluno()) {
            novoStatus = AgendamentoStatus.CANCELADO_CLIENTE;
            tipo = Role.ALUNO.name();
        } else if (usuario.isPersonal()) {
            novoStatus = AgendamentoStatus.CANCELADO_PERSONAL;
            tipo = Role.PERSONAL.name();
        } else {
            throw new UsuarioNaoEncontradoException();
        }

        agendamentoRepository.cancelarTodosAgendamentosPorUsuario(id, tipo, novoStatus);
    }
}
