package com.duyduc.workout_tracker.service.impl;

import com.duyduc.workout_tracker.auth.entity.User;
import com.duyduc.workout_tracker.auth.repository.UserRepo;
import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.entity.WorkoutPlan;
import com.duyduc.workout_tracker.exception.ResourceNotFoundException;
import com.duyduc.workout_tracker.repository.WorkoutPlanRepo;
import com.duyduc.workout_tracker.service.WorkoutPlanService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final UserRepo userRepo;
    private final WorkoutPlanRepo workoutPlanRepo;

    @Transactional
    @Override
    public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        workoutPlan.setUser(user);

        WorkoutPlan savedWorkout = workoutPlanRepo.save(workoutPlan);

        WorkoutPlanResponse response = WorkoutPlanResponse.builder()
                .id(savedWorkout.getId())
                .name(savedWorkout.getName())
                .description(savedWorkout.getDescription())
                .createdAt(savedWorkout.getCreatedAt())
                .build();

        return response;
    }
}
