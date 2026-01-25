package com.duyduc.workout_tracker.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SessionExerciseRequest {

    @NotNull(message = "Exercise ID is required")
    private Integer exerciseId;

    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets;

    @Min(value = 1, message = "Reps must be at least 1")
    private Integer reps;

    @DecimalMin(value = "0.0", message = "Weight must be non-negative")
    private BigDecimal weight;

    @Min(value = 0, message = "Duration must be non-negative")
    private Integer durationMinutes;

    @Min(value = 1, message = "Order index must start from 1")
    @NotNull(message = "Order index is required")
    private Integer orderIndex;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
