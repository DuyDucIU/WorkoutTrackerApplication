package com.duyduc.workout_tracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SessionExerciseResponse {
    private Integer id;
    private Integer workoutSessionId;
    private Integer exerciseId;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private BigDecimal weight;
    private Integer durationMinutes;
    private String notes;
    private Boolean completed;
}
