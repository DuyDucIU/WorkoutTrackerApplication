package com.duyduc.workout_tracker.service.impl;

import com.duyduc.workout_tracker.auth.entity.User;
import com.duyduc.workout_tracker.auth.repository.UserRepo;
import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.entity.WorkoutPlan;
import com.duyduc.workout_tracker.exception.ResourceNotFoundException;
import com.duyduc.workout_tracker.mapper.WorkoutPlanMapper;
import com.duyduc.workout_tracker.repository.WorkoutPlanRepo;
import com.duyduc.workout_tracker.service.WorkoutPlanService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final UserRepo userRepo;
    private final WorkoutPlanRepo workoutPlanRepo;
    private final WorkoutPlanMapper workoutPlanMapper;

    @Transactional
    @Override
    public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        WorkoutPlan savedWorkout = workoutPlanRepo.save(workoutPlan);

        WorkoutPlanResponse response = WorkoutPlanResponse.builder()
                .id(savedWorkout.getId())
                .name(savedWorkout.getName())
                .description(savedWorkout.getDescription())
                .createdAt(savedWorkout.getCreatedAt())
                .build();

        return response;
    }

    @Override
    public List<WorkoutPlanResponse> getWorkoutPlans(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<WorkoutPlan> workoutPlans = workoutPlanRepo.findByUserId(userId);

        List<WorkoutPlanResponse> response = workoutPlans.stream()
                                            .map(workoutPlan -> workoutPlanMapper.toWorkoutPlanResponse(workoutPlan))
                                            .collect(Collectors.toList());

        return response;
    }

    @Override
    public WorkoutPlanResponse getWorkoutPlanById(Integer id, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        WorkoutPlan workoutPlan = workoutPlanRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        WorkoutPlanResponse response = workoutPlanMapper.toWorkoutPlanResponse(workoutPlan);

        return response;
    }

    @Transactional
    @Override
    public WorkoutPlanResponse updateWorkoutPlanById(WorkoutPlanRequest request, Integer id, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        WorkoutPlan workoutPlan = workoutPlanRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        if(request.getName() != null && !request.getName().isBlank()) workoutPlan.setName(request.getName());

        workoutPlan.setDescription(request.getDescription());

        WorkoutPlan savedWorkout = workoutPlanRepo.save(workoutPlan);

        WorkoutPlanResponse response = workoutPlanMapper.toWorkoutPlanResponse(savedWorkout);

        return response;
    }
}
