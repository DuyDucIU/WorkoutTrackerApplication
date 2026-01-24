package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.WorkoutSessionRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutSessionResponse;
import com.duyduc.workout_tracker.entity.WorkoutSessionStatus;

import java.util.List;

public interface WorkoutSessionService {

        WorkoutSessionResponse createWorkoutSession(WorkoutSessionRequest request, Integer workoutPlanId,
                        Integer userId);

        List<WorkoutSessionResponse> getWorkoutSessions(Integer workoutPlanId, Integer userId);

        WorkoutSessionResponse getWorkoutSessionById(Integer id, Integer workoutPlanId, Integer userId);

        WorkoutSessionResponse updateWorkoutSession(WorkoutSessionRequest request, Integer id, Integer workoutPlanId,
                        Integer userId);

        WorkoutSessionResponse updateStatus(Integer id, Integer workoutPlanId, Integer userId,
                        WorkoutSessionStatus status);

        void deleteWorkoutSession(Integer id, Integer workoutPlanId, Integer userId);

        void deleteAllWorkoutSessions(Integer workoutPlanId, Integer userId);
}
