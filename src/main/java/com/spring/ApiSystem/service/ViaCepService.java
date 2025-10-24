package com.spring.ApiSystem.service;

import com.spring.ApiSystem.model.CEP;
import com.spring.ApiSystem.repository.CepRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class ViaCepService {
    private final CepRepository cepRepository;

    public ViaCepService(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    public CEP cadastrarCEP(String cep){
        RestTemplate rest = new RestTemplate();

        try{
            String url = "https://viacep.com.br/ws/" +
                         cep +
                         "/json/";
            ResponseEntity<CEP> resposta = rest.getForEntity(url, CEP.class);
            CEP respostaCorpo = resposta.getBody();
            respostaCorpo.setId(cep);
            cepRepository.save(respostaCorpo);
            return respostaCorpo;
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao acessar o ViaCEP");
        }
    }

    public CEP procurarCEP(String cep){
        Optional<CEP> cepBanco = cepRepository.findById(cep);
        return cepBanco.orElseGet(() -> cadastrarCEP(cep));
    }
}
