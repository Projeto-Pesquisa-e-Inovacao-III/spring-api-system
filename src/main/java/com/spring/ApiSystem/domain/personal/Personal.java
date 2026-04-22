package com.spring.ApiSystem.domain.personal;

import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import jakarta.persistence.*;

import java.util.List;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "personal")
public class Personal {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Usuario usuario;

    @Column(unique = true)
    private String cref;

    @Column(name = "buffer_minutos")
    private Integer bufferMinutos;

    private boolean profileAtivo = true;

    public Personal() {
        this.usuario = new Usuario();
    }

    public Personal(Long id, Usuario usuario, String cref, Integer bufferMinutos) {
        this(id, usuario, cref, bufferMinutos, true);
    }

    public Personal(Long id, Usuario usuario, String cref, Integer bufferMinutos, boolean profileAtivo) {
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário não pode ser nulo.");
        }
        this.id = id;
        usuario.setPersonal(this);
        this.cref = cref;
        this.bufferMinutos = bufferMinutos;
        this.profileAtivo = profileAtivo;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }

    public Integer getBufferMinutos() {
        return bufferMinutos;
    }

    public void setBufferMinutos(Integer bufferMinutos) {
        this.bufferMinutos = bufferMinutos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isPersonal() {
        return usuario.isPersonal();
    }

    public List<Telefone> getTelefones() {return usuario.getTelefones();}

    public void setTelefones(List<Telefone> telefones) {this.usuario.setTelefones(telefones);}

    public String getSenha(){
        return usuario.getSenha();
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
