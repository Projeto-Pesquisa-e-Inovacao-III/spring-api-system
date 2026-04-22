package com.spring.ApiSystem.domain.aluno;


import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.aluno.vo.CpfConverter;
import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;

import com.spring.ApiSystem.domain.usuario.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Usuario usuario;

    @Convert(converter = CpfConverter.class)
    @Column(unique = true, nullable = false, length = 11)
    private Cpf cpf;

    @Column(nullable = false)
    private boolean ativoAnamnese = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "anamnese_id")
    private Anamnese anamnese;

    private boolean profileAtivo = true;

    public Aluno() {
        this.usuario = new Usuario();
    }

    public Aluno(Long id, Usuario usuario, Cpf cpf, boolean ativoAnamnese, Anamnese anamnese, boolean profileAtivo) {
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário não pode ser nulo.");
        }
        this.id = id;
        usuario.setAluno(this);
        this.cpf = cpf;
        this.ativoAnamnese = ativoAnamnese;
        this.anamnese = anamnese;
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

    public boolean isAtivoAnamnese() {
        return ativoAnamnese;
    }

    public Integer getIdade() {
        return Period.between(getDataNascimento(), LocalDate.now()).getYears();
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        this.cpf = cpf;
    }

    public boolean getAtivoAnamnese() {
        return ativoAnamnese;
    }

    public void setAtivoAnamnese(boolean ativoAnamnese) {
        this.ativoAnamnese = ativoAnamnese;
    }

    public Anamnese getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(Anamnese anamnese) {
        this.anamnese = anamnese;
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

    public List<Telefone> getTelefones() {return usuario.getTelefones();}

    public void setTelefones(List<Telefone> telefones) {this.usuario.setTelefones(telefones);}

    public String getSenha(){
        return usuario.getSenha();
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
