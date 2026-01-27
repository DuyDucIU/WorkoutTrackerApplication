package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.WorkoutSessionRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutSessionResponse;
import com.duyduc.workout_tracker.entity.WorkoutSessionStatus;

import java.util.List;

public interface WorkoutSessionService {

        WorkoutSessionResponse createWorkoutSession(WorkoutSessionRequest request, Integer workoutPlanId,
                        Integer userId);

        List<WorkoutSessionResponse> getWorkoutSessions(Integer workoutPlanId, Integer userId);

        WorkoutSessionResponse getWorkoutSessionById(Integer sessionId, Integer workoutPlanId, Integer userId);

        WorkoutSessionResponse updateWorkoutSession(WorkoutSessionRequest request, Integer sessionId, Integer workoutPlanId,
                                                    Integer userId);

        WorkoutSessionResponse updateStatus(Integer sessionId, Integer workoutPlanId, Integer userId,
                                            WorkoutSessionStatus status);

        void deleteWorkoutSession(Integer sessionId, Integer workoutPlanId, Integer userId);

        void deleteAllWorkoutSessions(Integer workoutPlanId, Integer userId);
}
