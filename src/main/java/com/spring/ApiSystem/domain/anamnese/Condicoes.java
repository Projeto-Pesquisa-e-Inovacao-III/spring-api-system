package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.anamnese.enums.TipoCondicoes;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Condicoes {
    private String situacao;

    @Enumerated(EnumType.STRING)
    private TipoCondicoes tipo;

    public Condicoes() {}  // <-- obrigatório pro JPA

    public Condicoes(String situacao, TipoCondicoes tipo) {
        this.situacao = situacao;
        this.tipo = tipo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public TipoCondicoes getTipo() {
        return tipo;
    }

    public void setTipo(TipoCondicoes tipo) {
        this.tipo = tipo;
    }
}
