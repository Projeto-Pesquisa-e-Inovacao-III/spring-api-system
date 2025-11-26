package com.spring.ApiSystem.produtocontratado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProdutoContratadoScheduler {
    private final ProdutoContratadoRepository produtoContratadoRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProdutoContratadoScheduler.class);

    public ProdutoContratadoScheduler(ProdutoContratadoRepository produtoContratadoRepository) {
        this.produtoContratadoRepository = produtoContratadoRepository;
    }

    // Executa todo dia à meia-noite
    @Scheduled(cron = "0 0 0 * * *")
    public void verificarExpiracaoProdutosContratados(){
        logger.info("Iniciando verificação de expiração de produtos contratados");
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findBySituacao(true);
        int contador = 0;
        for(ProdutoContratado produtoContratado : produtosContratados){
            if(produtoContratado.getDataExpiracao().isBefore(LocalDate.now()) &&
                    produtoContratado.getSituacao().equals(true)){
                produtoContratado.setSituacao(false);
                produtoContratado.setSaldoAula(0);
                produtoContratadoRepository.save(produtoContratado);
                contador++;
            }
        }
        logger.info("Verificação concluída. De " + produtosContratados.size() +
                " produtos contratados, " + contador + " foram desativados.");
    }
}
