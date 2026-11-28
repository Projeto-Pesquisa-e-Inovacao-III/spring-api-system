package com.spring.ApiSystem.domain.exercicio;

import com.spring.ApiSystem.domain.musculo.Musculo;
import jakarta.persistence.*;

@Entity
@Table(name = "exercise_muscle")
public class ExercicioMusculo {
    @EmbeddedId
    private ExercicioMusculoId id = new ExercicioMusculoId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercicio exercicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("muscleId")
    @JoinColumn(name = "muscle_id", nullable = false)
    private Musculo musculo;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    public ExercicioMusculo() {}

    public ExercicioMusculo(Exercicio exercicio, Musculo musculo, String type) {
        this.exercicio = exercicio;
        this.musculo = musculo;
        this.type = type;
        this.id = new ExercicioMusculoId(exercicio.getId(), musculo.getId());
    }

    public ExercicioMusculoId getId() { return id; }
    public void setId(ExercicioMusculoId id) { this.id = id; }
    public Exercicio getExercicio() { return exercicio; }
    public void setExercicio(Exercicio exercicio) { this.exercicio = exercicio; }
    public Musculo getMusculo() { return musculo; }
    public void setMusculo(Musculo value) { this.musculo = value; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
