package com.spring.ApiSystem.domain.musculo;

import com.spring.ApiSystem.domain.exercicio.ExercicioMusculo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "muscle")
public class Musculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "musculo", fetch = FetchType.LAZY)
    private List<ExercicioMusculo> exercicios = new ArrayList<>();

    public Musculo() {}

    public Musculo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ExercicioMusculo> getExercicios() { return exercicios; }
    public void setExercicios(List<ExercicioMusculo> exercicios) { this.exercicios = exercicios; }
}
