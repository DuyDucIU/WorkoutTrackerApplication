package com.duyduc.workout_tracker.service.impl;

import com.duyduc.workout_tracker.entity.SessionExercise;
import com.duyduc.workout_tracker.entity.User;
import com.duyduc.workout_tracker.entity.WorkoutSession;
import com.duyduc.workout_tracker.repository.SessionExerciseRepo;
import com.duyduc.workout_tracker.repository.UserRepo;
import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.entity.WorkoutPlan;
import com.duyduc.workout_tracker.exception.ResourceNotFoundException;
import com.duyduc.workout_tracker.mapper.WorkoutPlanMapper;
import com.duyduc.workout_tracker.repository.WorkoutPlanRepo;
import com.duyduc.workout_tracker.repository.WorkoutSessionRepo;
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
    private final WorkoutSessionRepo workoutSessionRepo;
    private final SessionExerciseRepo sessionExerciseRepo;

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

        WorkoutPlanResponse response = workoutPlanMapper.toWorkoutPlanResponse(savedWorkout);

        return response;
    }

    @Transactional
    @Override
    public WorkoutPlanResponse copyWorkoutPlan(Integer id, Integer userId) {
        WorkoutPlan oldPlan = workoutPlanRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        WorkoutPlan newPlan = WorkoutPlan.builder()
                .name(oldPlan.getName() + " (Copy)")
                .description(oldPlan.getDescription())
                .user(oldPlan.getUser())
                .build();

        for (WorkoutSession oldSession : oldPlan.getSessions()) {

            WorkoutSession newSession = WorkoutSession.builder()
                    .name(oldSession.getName())
                    .notes(oldSession.getNotes())
                    .scheduledDate(oldSession.getScheduledDate())
                    .workoutPlan(newPlan)
                    .build();

            for (SessionExercise oldEx : oldSession.getExercises()) {
                newSession.getExercises().add(
                        SessionExercise.builder()
                                .workoutSession(newSession)
                                .exercise(oldEx.getExercise())
                                .sets(oldEx.getSets())
                                .reps(oldEx.getReps())
                                .weight(oldEx.getWeight())
                                .durationMinutes(oldEx.getDurationMinutes())
                                .orderIndex(oldEx.getOrderIndex())
                                .notes(oldEx.getNotes())
                                .build()
                );
            }

            newPlan.getSessions().add(newSession);
        }

        WorkoutPlan savedWorkout = workoutPlanRepo.save(newPlan);

        WorkoutPlanResponse response = workoutPlanMapper.toWorkoutPlanResponse(savedWorkout);

        return response;
    }

    @Override
    public List<WorkoutPlanResponse> getWorkoutPlans(Integer userId) {

        List<WorkoutPlan> workoutPlans = workoutPlanRepo.findByUserId(userId);

        List<WorkoutPlanResponse> response = workoutPlans.stream()
                .map(workoutPlan -> workoutPlanMapper.toWorkoutPlanResponse(workoutPlan))
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public WorkoutPlanResponse getWorkoutPlanById(Integer id, Integer userId) {

        WorkoutPlan workoutPlan = workoutPlanRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        WorkoutPlanResponse response = workoutPlanMapper.toWorkoutPlanResponse(workoutPlan);

        return response;
    }

    @Transactional
    @Override
    public WorkoutPlanResponse updateWorkoutPlanById(WorkoutPlanRequest request, Integer id, Integer userId) {

        WorkoutPlan workoutPlan = workoutPlanRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        if (request.getName() != null && !request.getName().isBlank())
            workoutPlan.setName(request.getName());

        workoutPlan.setDescription(request.getDescription());

        WorkoutPlan savedWorkout = workoutPlanRepo.save(workoutPlan);

        WorkoutPlanResponse response = workoutPlanMapper.toWorkoutPlanResponse(savedWorkout);

        return response;
    }

    @Transactional
    @Override
    public void deleteWorkoutPlanById(Integer id, Integer userId) {

        WorkoutPlan workoutPlan = workoutPlanRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        workoutPlanRepo.delete(workoutPlan);
    }

    @Transactional
    @Override
    public void deleteAllWorkoutPlans(Integer userId) {
        workoutPlanRepo.deleteByUserId(userId);
    }
}
