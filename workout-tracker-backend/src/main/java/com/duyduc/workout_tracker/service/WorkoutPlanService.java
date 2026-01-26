package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface WorkoutPlanService {
    WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request, Integer userId);
    WorkoutPlanResponse copyWorkoutPlan(Integer id, Integer userId);
    List<WorkoutPlanResponse> getWorkoutPlans(Integer userId);
    WorkoutPlanResponse getWorkoutPlanById(Integer id, Integer userId);
    WorkoutPlanResponse updateWorkoutPlanById(WorkoutPlanRequest request, Integer id, Integer userId);
    void deleteWorkoutPlanById(Integer id, Integer userId);
    void deleteAllWorkoutPlans(Integer userId);
}
