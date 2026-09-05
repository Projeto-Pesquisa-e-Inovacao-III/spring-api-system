package com.spring.ApiSystem.external.comprar;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.mock.payment.enabled", havingValue = "true")
public class ComprarServiceMock implements ComprarService{


    private final JpaUserDetailsService jpaUserDetailsService;
    private final ProdutoContratadoService produtoContratadoService;

    public ComprarServiceMock(JpaUserDetailsService jpaUserDetailsService, ProdutoContratadoService produtoContratadoService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.produtoContratadoService = produtoContratadoService;
    }

    @Override
    public String comprar(Long produtoExibicaoId) {
        Aluno aluno = jpaUserDetailsService.getCurrentAluno();
        produtoContratadoService.criarProdutoContratadoPeloAluno(produtoExibicaoId, aluno);
        return "Pagamento Mockado";
    }
}
