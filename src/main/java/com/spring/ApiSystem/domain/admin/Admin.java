package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "admin")
public class Admin  {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Usuario usuario;

    private boolean profileAtivo = true;

    public Admin() {
    }

    public Admin(Long id, Usuario usuario) {
        this(id, usuario, true);
    }

    public Admin(Long id, Usuario usuario, boolean profileAtivo) {
        if(usuario == null){
            throw new IllegalArgumentException("O usuário não pode ser nulo.");
        }
        this.id = id;
        usuario.setAdmin(this);
        this.profileAtivo = profileAtivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Telefone> getTelefones() {return usuario.getTelefones();}

    public void setTelefones(List<Telefone> telefones) {this.usuario.setTelefones(telefones);}

    public String getNome() {
        return usuario.getNome();
    }

    public String getEmail() {
        return usuario.getEmail();
    }

    public String getSexo() {
        return usuario.getSexo();
    }

    public LocalDate getDataNascimento() {
        return usuario.getDataNascimento();
    }

    public boolean isAtivo() {
        return usuario.isAtivo();
    }

    public Set<Role> getRoles() {
        return usuario.getRoles();
    }

    public void setAtivo(boolean ativo) {
        this.usuario.setAtivo(ativo);
    }

    public void setSenha(String senha) {
        this.usuario.setSenha(senha);
    }

    public void setSalt(String salt) {
        this.usuario.setSalt(salt);
    }

    public void setEmail(String email) {
        this.usuario.setEmail(email);
    }

    public void setSexo(String sexo) {
        this.usuario.setSexo(sexo);
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.usuario.setDataNascimento(dataNascimento);
    }

    public void setNome(String nome) {
        this.usuario.setNome(nome);
    }

    public boolean isProfileAtivo() {
        return profileAtivo;
    }

    public void setProfileAtivo(boolean profileAtivo) {
        this.profileAtivo = profileAtivo;
    }

    public String getCaminhoFoto() {
        return this.usuario.getCaminhoFoto();
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.usuario.setCaminhoFoto(caminhoFoto);
    }
}
