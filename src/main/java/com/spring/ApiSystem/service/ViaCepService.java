package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {
    public EnderecoDTO verificarCep(String cep){
        RestTemplate rest = new RestTemplate();

        try{
            String url = "https://viacep.com.br/ws/" +
                         cep +
                         "/json/";
            ResponseEntity<EnderecoDTO> resposta = rest.getForEntity(url, EnderecoDTO.class);
            EnderecoDTO respostaCorpo = resposta.getBody();
            respostaCorpo.setCep(respostaCorpo.getCep().replace("-", ""));
            return respostaCorpo;
        } catch (RuntimeException e) {
            throw new RuntimeException("Não foi possível encontrar o CEP informado");
        }
    }
}
