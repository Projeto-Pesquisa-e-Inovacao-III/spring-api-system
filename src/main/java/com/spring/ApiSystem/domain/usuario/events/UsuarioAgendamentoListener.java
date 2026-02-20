package com.spring.ApiSystem.eventos.usuario;

import com.spring.ApiSystem.domain.agendamento.AgendamentoRepository;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.TipoUsuario;
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
        if (usuario == null || usuario.getTipo() == null) {
            throw new UsuarioNaoEncontradoException();
        }

        long id = usuario.getId();
        TipoUsuario tipo = usuario.getTipo();

        AgendamentoStatus novoStatus;
        if (tipo == TipoUsuario.ALUNO) {
            novoStatus = AgendamentoStatus.CANCELADO_CLIENTE;
        } else if (tipo == TipoUsuario.PERSONAL) {
            novoStatus = AgendamentoStatus.CANCELADO_PERSONAL;
        } else {
            throw new UsuarioNaoEncontradoException();
        }

        agendamentoRepository.cancelarTodosAgendamentosPorUsuario(id, tipo.name(), novoStatus);
    }
}
