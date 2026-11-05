package com.spring.ApiSystem.domain.exercicio;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExercicioMusculoSecundarioId implements Serializable {
    private Long exerciseId;
    private Long secondaryMuscleId;

    public ExercicioMusculoSecundarioId() {}

    public ExercicioMusculoSecundarioId(Long exerciseId, Long secondaryMuscleId) {
        this.exerciseId = exerciseId;
        this.secondaryMuscleId = secondaryMuscleId;
    }

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public Long getSecondaryMuscleId() { return secondaryMuscleId; }
    public void setSecondaryMuscleId(Long secondaryMuscleId) { this.secondaryMuscleId = secondaryMuscleId; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ExercicioMusculoSecundarioId other)) return false;
        return Objects.equals(exerciseId, other.exerciseId)
                && Objects.equals(secondaryMuscleId, other.secondaryMuscleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, secondaryMuscleId);
    }
}
