package com.spring.ApiSystem.config;

import com.spring.ApiSystem.eventos.produtocontratado.ProdutoContrataListener;
import com.spring.ApiSystem.eventos.produtocontratado.ProdutoContratadoEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EventConfig {

    @Bean
    public ProdutoContratadoEventPublisher produtoContratadoEventPublisher(
            List<ProdutoContrataListener> listeners
    ) {
        return new ProdutoContratadoEventPublisher(listeners);
    }
}

