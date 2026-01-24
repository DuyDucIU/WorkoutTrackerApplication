package com.duyduc.workout_tracker.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkoutSessionRequest {
    private String name;
    private LocalDateTime scheduledDate;
    private String notes;
}
