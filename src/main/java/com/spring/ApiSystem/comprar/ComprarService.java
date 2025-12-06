package com.spring.ApiSystem.comprar;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.comprar.dto.LinkDto;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ComprarService {

    private final JpaUserDetailsService userDetailsService;
    private final RestTemplate restTemplate;

    @Value("${pag.url}")
    private String url;

    public ComprarService(JpaUserDetailsService userDetailsService, RestTemplate restTemplate) {
        this.userDetailsService = userDetailsService;
        this.restTemplate = restTemplate;
    }


    public LinkDto comprar(Long produtoExibicaoId){

        //TODO: validar se produtoExibicao Existe

        Long userId = userDetailsService.getCurrentAluno().getId();

        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("user_id", userId);
        requestBody.put("produto_exibicao_id", produtoExibicaoId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Long>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(url+"/checkouts/simple", requestEntity, LinkDto.class).getBody();
    }
}
