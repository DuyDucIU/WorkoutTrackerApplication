package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;

public interface WorkoutPlanService {
    WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request, Integer userId);
}
