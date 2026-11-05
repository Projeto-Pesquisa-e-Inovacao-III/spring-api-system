package com.spring.ApiSystem.domain.exercicio;

import com.spring.ApiSystem.domain.musculosecundario.MusculoSecundario;
import jakarta.persistence.*;

@Entity
@Table(name = "exercise_secondary_muscle")
public class ExercicioMusculoSecundario {
    @EmbeddedId
    private ExercicioMusculoSecundarioId id = new ExercicioMusculoSecundarioId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercicio exercicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("secondaryMuscleId")
    @JoinColumn(name = "secondary_muscle_id", nullable = false)
    private MusculoSecundario musculoSecundario;

    public ExercicioMusculoSecundario() {}

    public ExercicioMusculoSecundario(Exercicio exercicio, MusculoSecundario musculoSecundario) {
        this.exercicio = exercicio;
        this.musculoSecundario = musculoSecundario;
        this.id = new ExercicioMusculoSecundarioId(exercicio.getId(), musculoSecundario.getId());
    }

    public ExercicioMusculoSecundarioId getId() { return id; }
    public void setId(ExercicioMusculoSecundarioId id) { this.id = id; }
    public Exercicio getExercicio() { return exercicio; }
    public void setExercicio(Exercicio exercicio) { this.exercicio = exercicio; }
    public MusculoSecundario getMusculoSecundario() { return musculoSecundario; }
    public void setMusculoSecundario(MusculoSecundario value) { this.musculoSecundario = value; }
}
