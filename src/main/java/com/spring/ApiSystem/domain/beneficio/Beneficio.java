package com.spring.ApiSystem.domain.beneficio;

import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "beneficios")

public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Valor do benefício não pode estar vazio")
    @Size(max = 50, message = "O benefício deve conter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String valor;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_exibicao_id", nullable = false)
    private ProdutoExibicao produtoExibicao;

    public Beneficio() {
    }

    public Beneficio(Long id, String valor, ProdutoExibicao produtoExibicao) {
        this.id = id;
        this.valor = valor;
        this.produtoExibicao = produtoExibicao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public ProdutoExibicao getProdutoExibicao() {
        return produtoExibicao;
    }

    public void setProdutoExibicao(ProdutoExibicao produtoExibicao) {
        this.produtoExibicao = produtoExibicao;
    }
}
