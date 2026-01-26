package com.duyduc.workout_tracker.service.impl;

import com.duyduc.workout_tracker.dto.request.WorkoutSessionRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutSessionResponse;
import com.duyduc.workout_tracker.entity.*;
import com.duyduc.workout_tracker.exception.ResourceNotFoundException;
import com.duyduc.workout_tracker.mapper.WorkoutSessionMapper;
import com.duyduc.workout_tracker.repository.ExerciseRepo;
import com.duyduc.workout_tracker.repository.WorkoutPlanRepo;
import com.duyduc.workout_tracker.repository.WorkoutSessionRepo;
import com.duyduc.workout_tracker.service.WorkoutSessionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

    private final WorkoutSessionRepo workoutSessionRepo;
    private final WorkoutPlanRepo workoutPlanRepo;
    private final ExerciseRepo exerciseRepo;
    private final WorkoutSessionMapper workoutSessionMapper;

    private WorkoutPlan getWorkoutPlanAndValidateOwner(Integer workoutPlanId, Integer userId) {
        WorkoutPlan workoutPlan = workoutPlanRepo.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + workoutPlanId));

        if (!workoutPlan.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout plan not found with id: " + workoutPlanId);
        }

        return workoutPlan;
    }

    @Transactional
    @Override
    public WorkoutSessionResponse createWorkoutSession(WorkoutSessionRequest request, Integer workoutPlanId,
            Integer userId) {
        WorkoutPlan workoutPlan = getWorkoutPlanAndValidateOwner(workoutPlanId, userId);

        WorkoutSession workoutSession = WorkoutSession.builder()
                .workoutPlan(workoutPlan)
                .name(request.getName())
                .scheduledDate(request.getScheduledDate())
                .notes(request.getNotes())
                .build();

        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            List<SessionExercise> sessionExercises = request.getExercises().stream().map(exRequest -> {
                Exercise exercise = exerciseRepo.findById(exRequest.getExerciseId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Exercise not found with id: " + exRequest.getExerciseId()));

                return SessionExercise.builder()
                        .workoutSession(workoutSession)
                        .exercise(exercise)
                        .sets(exRequest.getSets())
                        .reps(exRequest.getReps())
                        .weight(exRequest.getWeight())
                        .durationMinutes(exRequest.getDurationMinutes())
                        .orderIndex(exRequest.getOrderIndex())
                        .notes(exRequest.getNotes())
                        .build();
            }).collect(Collectors.toList());

            workoutSession.setExercises(sessionExercises);
        }

        WorkoutSession savedSession = workoutSessionRepo.save(workoutSession);
        return workoutSessionMapper.toWorkoutSessionResponse(savedSession);
    }

    @Override
    public List<WorkoutSessionResponse> getWorkoutSessions(Integer workoutPlanId, Integer userId) {
        getWorkoutPlanAndValidateOwner(workoutPlanId, userId);

        List<WorkoutSession> sessions = workoutSessionRepo.findByWorkoutPlanId(workoutPlanId);
        return sessions.stream()
                .map(workoutSessionMapper::toWorkoutSessionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutSessionResponse getWorkoutSessionById(Integer id, Integer workoutPlanId, Integer userId) {
        getWorkoutPlanAndValidateOwner(workoutPlanId, userId);

        WorkoutSession session = workoutSessionRepo.findByIdAndWorkoutPlanId(id, workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found with id: " + id));

        return workoutSessionMapper.toWorkoutSessionResponse(session);
    }

    @Transactional
    @Override
    public WorkoutSessionResponse updateWorkoutSession(WorkoutSessionRequest request, Integer id, Integer workoutPlanId,
            Integer userId) {
        getWorkoutPlanAndValidateOwner(workoutPlanId, userId);

        WorkoutSession session = workoutSessionRepo.findByIdAndWorkoutPlanId(id, workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found with id: " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            session.setName(request.getName());
        }
        if (request.getScheduledDate() != null) {
            session.setScheduledDate(request.getScheduledDate());
        }
        if (request.getNotes() != null) {
            session.setNotes(request.getNotes());
        }

        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            List<SessionExercise> newExercises = request.getExercises().stream().map(exRequest -> {
                Exercise exercise = exerciseRepo.findById(exRequest.getExerciseId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Exercise not found with id: " + exRequest.getExerciseId()));

                return SessionExercise.builder()
                        .workoutSession(session)
                        .exercise(exercise)
                        .sets(exRequest.getSets())
                        .reps(exRequest.getReps())
                        .weight(exRequest.getWeight())
                        .durationMinutes(exRequest.getDurationMinutes())
                        .orderIndex(exRequest.getOrderIndex())
                        .notes(exRequest.getNotes())
                        .build();
            }).collect(Collectors.toList());

            session.getExercises().addAll(newExercises);
        }

        WorkoutSession updatedSession = workoutSessionRepo.save(session);
        return workoutSessionMapper.toWorkoutSessionResponse(updatedSession);
    }

    @Transactional
    @Override
    public WorkoutSessionResponse updateStatus(Integer id, Integer workoutPlanId, Integer userId,
            WorkoutSessionStatus status) {
        getWorkoutPlanAndValidateOwner(workoutPlanId, userId);

        WorkoutSession session = workoutSessionRepo.findByIdAndWorkoutPlanId(id, workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found with id: " + id));

        session.setStatus(status);
        WorkoutSession updatedSession = workoutSessionRepo.save(session);
        return workoutSessionMapper.toWorkoutSessionResponse(updatedSession);
    }

    @Transactional
    @Override
    public void deleteWorkoutSession(Integer id, Integer workoutPlanId, Integer userId) {
        getWorkoutPlanAndValidateOwner(workoutPlanId, userId);

        WorkoutSession session = workoutSessionRepo.findByIdAndWorkoutPlanId(id, workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found with id: " + id));

        workoutSessionRepo.delete(session);
    }

    @Transactional
    @Override
    public void deleteAllWorkoutSessions(Integer workoutPlanId, Integer userId) {
        getWorkoutPlanAndValidateOwner(workoutPlanId, userId);
        workoutSessionRepo.deleteAllByWorkoutPlanId(workoutPlanId);
    }
}
