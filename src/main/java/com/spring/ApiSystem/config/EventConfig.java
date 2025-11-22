package com.spring.ApiSystem.config;

import com.spring.ApiSystem.eventos.agendamentos.AgendamentoEventPublisher;
import com.spring.ApiSystem.eventos.agendamentos.AgendamentoListener;
import com.spring.ApiSystem.eventos.aluno.AlunoEventPublisher;
import com.spring.ApiSystem.eventos.aluno.AlunosListener;
import com.spring.ApiSystem.eventos.produtocontratado.ProdutoContrataListener;
import com.spring.ApiSystem.eventos.produtocontratado.ProdutoContratadoEventPublisher;
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
}

