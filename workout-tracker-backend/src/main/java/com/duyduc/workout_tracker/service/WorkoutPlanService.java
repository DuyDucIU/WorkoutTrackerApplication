package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;

import java.util.List;

public interface WorkoutPlanService {
    WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request, Integer userId);

    WorkoutPlanResponse copyWorkoutPlan(Integer planId, Integer userId);

    List<WorkoutPlanResponse> getWorkoutPlans(Integer userId);

    WorkoutPlanResponse getWorkoutPlanById(Integer planId, Integer userId);

    WorkoutPlanResponse updateWorkoutPlanById(WorkoutPlanRequest request, Integer planId, Integer userId);

    void deleteWorkoutPlanById(Integer planId, Integer userId);

}
