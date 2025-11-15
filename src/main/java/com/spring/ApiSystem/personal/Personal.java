package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.util.List;

import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Personal extends Usuario {
    @Column(unique = true)
    private String cref;

    public Personal() {
    }



    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }
}
