package com.spring.ApiSystem.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@Builder

public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private Long cpf;

    @Column(nullable = false)
    private String senha;
    private String nome;

    private boolean ativo = true;

    public UsuarioEntity(boolean ativo, Long cpf, String email, Long id, String nome, String senha) {
        this.ativo = ativo;
        this.cpf = cpf;
        this.email = email;
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public UsuarioEntity() {
    }
}
