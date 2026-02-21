package com.spring.ApiSystem.shared.config;

import com.spring.ApiSystem.domain.agendamento.events.AgendamentoEventPublisher;
import com.spring.ApiSystem.domain.agendamento.events.AgendamentoListener;
import com.spring.ApiSystem.domain.aluno.events.AlunoEventPublisher;
import com.spring.ApiSystem.domain.aluno.events.AlunosListener;
import com.spring.ApiSystem.domain.produtocontratado.events.ProdutoContrataListener;
import com.spring.ApiSystem.domain.produtocontratado.events.ProdutoContratadoEventPublisher;
import com.spring.ApiSystem.domain.usuario.events.UsuarioEventPublisher;
import com.spring.ApiSystem.domain.usuario.events.UsuarioListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EventConfig {

    @Bean
    public AgendamentoEventPublisher agendamentoEventPublisher(
            List<AgendamentoListener> listeners
    ) {
        return new AgendamentoEventPublisher(listeners);
    }

    @Bean
    public ProdutoContratadoEventPublisher produtoContratadoEventPublisher(
            List<ProdutoContrataListener> listeners
    ) {
        return new ProdutoContratadoEventPublisher(listeners);
    }

    @Bean
    public AlunoEventPublisher alunoEventPublisher(
            List<AlunosListener> listeners
    ) {
        return new AlunoEventPublisher(listeners);
    }

    @Bean
    public UsuarioEventPublisher usuarioEventPublisher(
            List<UsuarioListener> listeners
    ) {
        return new UsuarioEventPublisher(listeners);
    }


}

