package com.spring.ApiSystem.domain.musculosecundario;

import com.spring.ApiSystem.domain.exercicio.ExercicioMusculoSecundario;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "secondary_muscle")
public class MusculoSecundario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "musculoSecundario", fetch = FetchType.LAZY)
    private List<ExercicioMusculoSecundario> exercicios = new ArrayList<>();

    public MusculoSecundario() {}

    public MusculoSecundario(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ExercicioMusculoSecundario> getExercicios() { return exercicios; }
    public void setExercicios(List<ExercicioMusculoSecundario> exercicios) { this.exercicios = exercicios; }
}
