package com.spring.ApiSystem.domain.exercicio;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExercicioMusculoId implements Serializable {
    private Long exerciseId;
    private Long muscleId;

    public ExercicioMusculoId() {}

    public ExercicioMusculoId(Long exerciseId, Long muscleId) {
        this.exerciseId = exerciseId;
        this.muscleId = muscleId;
    }

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public Long getMuscleId() { return muscleId; }
    public void setMuscleId(Long muscleId) { this.muscleId = muscleId; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ExercicioMusculoId other)) return false;
        return Objects.equals(exerciseId, other.exerciseId)
                && Objects.equals(muscleId, other.muscleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, muscleId);
    }
}
