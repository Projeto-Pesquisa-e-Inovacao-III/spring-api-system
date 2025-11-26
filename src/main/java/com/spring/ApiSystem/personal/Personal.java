package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.util.List;

import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Personal extends Usuario {
    @Column(unique = true)
    private String cref;

    public Personal() {}

    public Personal(Long id, TipoUsuario tipo, String nome, String sexo, LocalDate dataNascimento, String email, String salt, String senha, boolean ativo, String caminhoFoto, List<Telefone> telefones, String cref) {
        super(id, TipoUsuario.PERSONAL, nome, sexo, dataNascimento, email, salt, senha, ativo, caminhoFoto, telefones);
        this.cref = cref;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }
}
