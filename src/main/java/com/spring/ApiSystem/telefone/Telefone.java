package com.spring.ApiSystem.telefone;

import com.spring.ApiSystem.usuario.Usuario;
import jakarta.persistence.*;

@Entity(name = "telefone")
public class Telefone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2, nullable = false)
    private String pais;

    @Column(length = 3, nullable = false)
    private String ddd;

    @Column(length = 9, nullable = false)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",
                nullable = false)
    private Usuario usuario;

    public Telefone() {}

    public Telefone(Long id, String pais,
                    String ddd, String numero,
                    Usuario usuario) {
        this.id = id;
        this.pais = pais;
        this.ddd = ddd;
        this.numero = numero;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
