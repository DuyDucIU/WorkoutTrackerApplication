package com.duyduc.workout_tracker.dto.response;

import com.duyduc.workout_tracker.entity.WorkoutSessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkoutSessionResponse {
    private Integer id;
    private Integer workoutPlanId;
    private String name;
    private LocalDateTime scheduledDate;
    private String notes;
    private WorkoutSessionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
