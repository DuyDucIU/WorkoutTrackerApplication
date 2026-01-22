package com.duyduc.workout_tracker.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanResponse {
    private Integer id;
    private String name;
    private String description;
    private Instant createdAt;
}
