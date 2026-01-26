package com.duyduc.workout_tracker.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkoutSessionRequest {

    @NotBlank(message = "Session name is required")
    @Size(max = 100, message = "Session name must not exceed 100 characters")
    private String name;

    private LocalDateTime scheduledDate;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @Valid
    private java.util.List<SessionExerciseRequest> exercises;
}
