package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity(name = "personal")
@DiscriminatorValue("Personal")
public class Personal extends Usuario {
    @Column(unique = true)
    private String cref;

    public Personal() {
    }


    public Personal(Long id, String tipo, String nome, String sexo,
                    LocalDate dataNascimento, String email, String salt,
                    String senha, boolean ativo, String cref) {
        super(id, tipo, nome, sexo, dataNascimento, email, salt, senha, ativo);
        this.cref = cref;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }
}
