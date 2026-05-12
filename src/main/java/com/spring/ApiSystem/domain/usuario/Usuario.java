package com.spring.ApiSystem.domain.usuario;

import com.spring.ApiSystem.domain.admin.Admin;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
//TODO: Composição invés de hierarquia
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sexo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(unique = true, nullable = false)
    private String email;

    private String salt;

    @Column(name = "senha_hash",
            nullable = false)
    private String senha;

    private boolean ativo = true;

    @Column(name= "caminho_foto")
    private String caminhoFoto;

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefone> telefones = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Personal personal;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Aluno aluno;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;

    public Usuario() {}

    public Usuario(Long id, String nome, Set<Role> roles, String sexo, LocalDate dataNascimento, String email, String salt, String senha, boolean ativo, String caminhoFoto, List<Telefone> telefones) {
        this.id = id;
        this.nome = nome;
        this.roles = roles;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.salt = salt;
        this.senha = senha;
        this.ativo = ativo;
        this.caminhoFoto = caminhoFoto;
        this.telefones = telefones;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Telefone> getTelefones() {return telefones;}

    public void setTelefones(List<Telefone> telefones) {this.telefones = telefones;}

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isPersonal(){
        return roles.contains(Role.PERSONAL);
    }

    public boolean isAluno(){
        return roles.contains(Role.ALUNO);
    }

    public boolean isAdmin(){
        return roles.contains(Role.ADMIN);
    }

    public boolean isDono() { return roles.contains(Role.DONO); }

    public boolean isNotRole(Role role){
        return !roles.contains(role);
    }

    public boolean isRole(Role role){
        return roles.contains(role);
    }

    public boolean hasOnlyRole(Role role){
        return roles.size() == 1 && roles.contains(role);
    }

    public void addRole(Role role) {
        if(isAluno() && role == Role.PERSONAL){
            throw new IllegalArgumentException("Um usuário do tipo ALUNO não pode ter a role PERSONAL.");
        }
        if(isPersonal() && role == Role.ALUNO){
            throw new IllegalArgumentException("Um usuário do tipo PERSONAL não pode ter a role ALUNO.");
        }
        if((isDono() || isAdmin()) && role == Role.ALUNO){
            throw new IllegalArgumentException("Um usuário do tipo ADMIN ou DONO não pode ter a role ALUNO.");
        }
        if(isRole(role)){
            throw new IllegalArgumentException("O usuário já possui a role: " + role);
        }
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if(!isRole(role)){
            throw new IllegalArgumentException("O usuário não possui a role: " + role);
        }
        if(isDono() && (role.equals(Role.DONO) || role.equals(Role.ADMIN))){
            throw new IllegalArgumentException("Um usuário do tipo DONO não pode ter a role DONO ou ADMIN removida.");
        }
        this.roles.remove(role);
    }

    public void setPersonal(Personal p) {
        this.personal = p;
        if (personal != null && personal.getUsuario() != this) {
            personal.setUsuario(this);
        }
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
        if(aluno != null && aluno.getUsuario() != this) {
            aluno.setUsuario(this);
        }
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        if (admin != null && admin.getUsuario() != this) {
            admin.setUsuario(this);
        }
    }

    public Personal getPersonal() {
        return personal;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Admin getAdmin() {
        return admin;
    }
}
