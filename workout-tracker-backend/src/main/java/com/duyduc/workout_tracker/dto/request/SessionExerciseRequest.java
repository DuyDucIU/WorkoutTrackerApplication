package com.duyduc.workout_tracker.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SessionExerciseRequest {
    private Integer exerciseId;
    private Integer sets;
    private Integer reps;
    private BigDecimal weight;
    private Integer durationMinutes;
    private Integer orderIndex;
    private String notes;
}
