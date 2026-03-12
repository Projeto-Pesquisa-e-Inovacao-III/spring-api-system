package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "anamnese")
public class Anamnese {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double altura;

    private Double peso;

    private String objectivoPrincipal;

    private String rotina;

    @ElementCollection
    @CollectionTable(name = "anamnese_condicoes", joinColumns = @JoinColumn(name = "anamnese_id"))
    @Column(name = "condicao")
    private List<String> condicoes;

    @Enumerated(EnumType.STRING)
    private NivelDeAtividadeEnum nivelDeAtividade;

    private String observacaoSaude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Anamnese(Long id, Double altura, Double peso, String objectivoPrincipal, String rotina, List<String> condicoes, NivelDeAtividadeEnum nivelDeAtividade, String observacaoSaude, Usuario usuario) {
        this.id = id;
        this.altura = altura;
        this.peso = peso;
        this.objectivoPrincipal = objectivoPrincipal;
        this.rotina = rotina;
        this.condicoes = condicoes;
        this.nivelDeAtividade = nivelDeAtividade;
        this.observacaoSaude = observacaoSaude;
        this.usuario = usuario;
    }

    public Anamnese() {
    }

    public Long getId() {
        return id;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getObjectivoPrincipal() {
        return objectivoPrincipal;
    }

    public void setObjectivoPrincipal(String objectivoPrincipal) {
        this.objectivoPrincipal = objectivoPrincipal;
    }

    public String getRotina() {
        return rotina;
    }

    public void setRotina(String rotina) {
        this.rotina = rotina;
    }

    public List<String> getCondicoes() {
        return condicoes;
    }

    public void setCondicoes(List<String> condicoes) {
        this.condicoes = condicoes;
    }

    public NivelDeAtividadeEnum getNivelDeAtividade() {
        return nivelDeAtividade;
    }

    public void setNivelDeAtividade(NivelDeAtividadeEnum nivelDeAtividade) {
        this.nivelDeAtividade = nivelDeAtividade;
    }

    public String getObservacaoSaude() {
        return observacaoSaude;
    }

    public void setObservacaoSaude(String observacaoSaude) {
        this.observacaoSaude = observacaoSaude;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
