package com.spring.ApiSystem.domain.cep;

import com.spring.ApiSystem.domain.cep.dto.response.CEPDto;
import com.spring.ApiSystem.domain.cep.dto.response.ViaCepDTO;
import com.spring.ApiSystem.domain.cep.exception.CepNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class ViaCepService {
    private final CepRepository cepRepository;

    public ViaCepService(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    public CEP cadastrarCEP(CEPDto cep){
        RestTemplate rest = new RestTemplate();
        String url = "https://viacep.com.br/ws/" + cep.id() + "/json/";

        ResponseEntity<ViaCepDTO> resposta;
        try {
            resposta = rest.getForEntity(url, ViaCepDTO.class);
        } catch (RestClientException e) {
            CEP fallback = new CEP();
            fallback.setId(cep.id());
            fallback.setLogradouro(cep.logradouro());
            fallback.setBairro(cep.bairro());
            fallback.setLocalidade(cep.localidade());
            fallback.setUf(cep.uf());
            cepRepository.save(fallback);
            return fallback;
        }

        ViaCepDTO respostaCorpo = resposta.getBody();
        if (respostaCorpo == null || Boolean.TRUE.equals(respostaCorpo.erro())) {
            throw new CepNaoEncontradoException();
        }

        CEP entidade = new CEP();
        entidade.setId(cep.id());
        entidade.setLogradouro(respostaCorpo.logradouro());
        entidade.setBairro(respostaCorpo.bairro());
        entidade.setLocalidade(respostaCorpo.localidade());
        entidade.setUf(respostaCorpo.uf());

        cepRepository.save(entidade);
        return entidade;
    }

    public CEP procurarCEP(CEPDto cep){
        Optional<CEP> cepBanco = cepRepository.findById(cep.id());
        return cepBanco.orElseGet(() -> cadastrarCEP(cep));
    }
}
