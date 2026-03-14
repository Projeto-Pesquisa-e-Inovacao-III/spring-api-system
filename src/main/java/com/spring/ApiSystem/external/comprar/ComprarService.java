package com.spring.ApiSystem.external.comprar;

import com.spring.ApiSystem.domain.aluno.Aluno;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicaoService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.external.comprar.exception.AlunoAlreadyHaveProdutoContratadoByTipoProdutoException;
import com.spring.ApiSystem.external.comprar.dto.response.LinkDto;
import com.spring.ApiSystem.external.comprar.exception.AlunoJaTemProdutoContratado;
import com.spring.ApiSystem.external.comprar.exception.CompraDeProdutoExibicaoInexistente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ComprarService {

    private final JpaUserDetailsService userDetailsService;
    private final RestTemplate restTemplate;
    private final ProdutoExibicaoService produtoExibicaoService;
    private final ProdutoContratadoService produtoContratadoService;

    @Value("${pag.url}")
    private String url;

    public ComprarService(JpaUserDetailsService userDetailsService, RestTemplate restTemplate, ProdutoExibicaoService produtoExibicaoService, ProdutoContratadoService produtoContratadoService) {
        this.userDetailsService = userDetailsService;
        this.restTemplate = restTemplate;
        this.produtoExibicaoService = produtoExibicaoService;
        this.produtoContratadoService = produtoContratadoService;
    }


    public LinkDto comprar(Long produtoExibicaoId){
        if(!produtoExibicaoService.produtoExibicaoAtivoExiste(produtoExibicaoId)){
            throw new CompraDeProdutoExibicaoInexistente(produtoExibicaoId);
        }

        Aluno aluno = userDetailsService.getCurrentAluno();
        Long userId = aluno.getId();

        produtoContratadoService.temProdutoContratadoTipoProdutoAtivo(
                produtoExibicaoId, aluno, TipoProduto.PACOTE
        );

        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("user_id", userId);
        requestBody.put("produto_exibicao_id", produtoExibicaoId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Long>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(url+"/checkouts/simple", requestEntity, LinkDto.class).getBody();
    }
}
