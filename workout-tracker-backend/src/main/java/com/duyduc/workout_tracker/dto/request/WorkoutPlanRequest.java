package com.duyduc.workout_tracker.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanRequest {
    private String name;
    private String description;
}
