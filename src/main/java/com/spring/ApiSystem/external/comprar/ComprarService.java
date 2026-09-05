package com.spring.ApiSystem.external.comprar;

import com.spring.ApiSystem.domain.telefone.Telefone;

public interface ComprarService {

    String comprar(Long produtoExibicaoId);

    default String formatCountryToGateway(Telefone telefone) {
        if (telefone == null || telefone.getPais() == null || telefone.getPais().isBlank()) {
            return "";
        }

        return telefone.getPais().startsWith("+") ? telefone.getPais() : "+" + telefone.getPais();
    }

}
