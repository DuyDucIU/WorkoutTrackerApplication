package com.duyduc.workout_tracker.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "session_exercises")
public class SessionExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private Integer sets;

    private Integer reps;

    @Column(precision = 6, scale = 2)
    private BigDecimal weight;

    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Boolean completed = false;

    @Builder
    public SessionExercise(WorkoutSession workoutSession, Exercise exercise, Integer sets,
            Integer reps, BigDecimal weight, Integer durationMinutes,
            Integer orderIndex, String notes) {
        this.workoutSession = workoutSession;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.durationMinutes = durationMinutes;
        this.orderIndex = orderIndex != null ? orderIndex : 0;
        this.notes = notes;
    }
}
