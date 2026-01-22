package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;

import java.util.List;

public interface WorkoutPlanService {
    WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request, Integer userId);
    List<WorkoutPlanResponse> getWorkoutPlans(Integer userId);
}
