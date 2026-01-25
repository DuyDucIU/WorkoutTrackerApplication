package com.duyduc.workout_tracker.service.impl;

import com.duyduc.workout_tracker.dto.request.SessionExerciseRequest;
import com.duyduc.workout_tracker.dto.response.SessionExerciseResponse;
import com.duyduc.workout_tracker.entity.Exercise;
import com.duyduc.workout_tracker.entity.SessionExercise;
import com.duyduc.workout_tracker.entity.WorkoutPlan;
import com.duyduc.workout_tracker.entity.WorkoutSession;
import com.duyduc.workout_tracker.exception.ResourceNotFoundException;
import com.duyduc.workout_tracker.mapper.SessionExerciseMapper;
import com.duyduc.workout_tracker.repository.ExerciseRepo;
import com.duyduc.workout_tracker.repository.SessionExerciseRepo;
import com.duyduc.workout_tracker.repository.WorkoutPlanRepo;
import com.duyduc.workout_tracker.repository.WorkoutSessionRepo;
import com.duyduc.workout_tracker.service.SessionExerciseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionExerciseServiceImpl implements SessionExerciseService {

    private final SessionExerciseRepo sessionExerciseRepo;
    private final WorkoutSessionRepo workoutSessionRepo;
    private final WorkoutPlanRepo workoutPlanRepo;
    private final ExerciseRepo exerciseRepo;
    private final SessionExerciseMapper sessionExerciseMapper;

    private WorkoutSession getWorkoutSessionAndValidateOwner(Integer sessionId, Integer planId, Integer userId) {
        // First validate the plan belongs to the user
        WorkoutPlan plan = workoutPlanRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + planId));

        if (!plan.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout plan not found with id: " + planId);
        }

        // Then validate the session belongs to the plan
        WorkoutSession session = workoutSessionRepo.findByIdAndWorkoutPlanId(sessionId, planId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found with id: " + sessionId));

        return session;
    }

    @Transactional
    @Override
    public SessionExerciseResponse createSessionExercise(SessionExerciseRequest request, Integer sessionId,
            Integer planId, Integer userId) {
        WorkoutSession session = getWorkoutSessionAndValidateOwner(sessionId, planId, userId);

        Exercise exercise = exerciseRepo.findById(request.getExerciseId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Exercise not found with id: " + request.getExerciseId()));

        SessionExercise sessionExercise = SessionExercise.builder()
                .workoutSession(session)
                .exercise(exercise)
                .sets(request.getSets())
                .reps(request.getReps())
                .weight(request.getWeight())
                .durationMinutes(request.getDurationMinutes())
                .orderIndex(request.getOrderIndex())
                .notes(request.getNotes())
                .build();

        SessionExercise savedExercise = sessionExerciseRepo.save(sessionExercise);
        return sessionExerciseMapper.toSessionExerciseResponse(savedExercise);
    }

    @Override
    public List<SessionExerciseResponse> getSessionExercises(Integer sessionId, Integer planId, Integer userId) {
        getWorkoutSessionAndValidateOwner(sessionId, planId, userId);

        List<SessionExercise> exercises = sessionExerciseRepo.findByWorkoutSessionIdOrderByOrderIndexAsc(sessionId);
        return exercises.stream()
                .map(sessionExerciseMapper::toSessionExerciseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SessionExerciseResponse getSessionExerciseById(Integer id, Integer sessionId, Integer planId,
            Integer userId) {
        getWorkoutSessionAndValidateOwner(sessionId, planId, userId);

        SessionExercise exercise = sessionExerciseRepo.findByIdAndWorkoutSessionId(id, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session exercise not found with id: " + id));

        return sessionExerciseMapper.toSessionExerciseResponse(exercise);
    }

    @Transactional
    @Override
    public SessionExerciseResponse updateSessionExercise(SessionExerciseRequest request, Integer id, Integer sessionId,
            Integer planId, Integer userId) {
        getWorkoutSessionAndValidateOwner(sessionId, planId, userId);

        SessionExercise exercise = sessionExerciseRepo.findByIdAndWorkoutSessionId(id, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session exercise not found with id: " + id));

        if (request.getExerciseId() != null) {
            Exercise newExercise = exerciseRepo.findById(request.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Exercise not found with id: " + request.getExerciseId()));
            exercise.setExercise(newExercise);
        }
        if (request.getSets() != null) {
            exercise.setSets(request.getSets());
        }
        if (request.getReps() != null) {
            exercise.setReps(request.getReps());
        }
        if (request.getWeight() != null) {
            exercise.setWeight(request.getWeight());
        }
        if (request.getDurationMinutes() != null) {
            exercise.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getOrderIndex() != null) {
            exercise.setOrderIndex(request.getOrderIndex());
        }
        if (request.getNotes() != null) {
            exercise.setNotes(request.getNotes());
        }

        SessionExercise updatedExercise = sessionExerciseRepo.save(exercise);
        return sessionExerciseMapper.toSessionExerciseResponse(updatedExercise);
    }

    @Transactional
    @Override
    public SessionExerciseResponse markAsCompleted(Integer id, Integer sessionId, Integer planId, Integer userId,
            Boolean completed) {
        getWorkoutSessionAndValidateOwner(sessionId, planId, userId);

        SessionExercise exercise = sessionExerciseRepo.findByIdAndWorkoutSessionId(id, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session exercise not found with id: " + id));

        exercise.setCompleted(completed);
        SessionExercise updatedExercise = sessionExerciseRepo.save(exercise);
        return sessionExerciseMapper.toSessionExerciseResponse(updatedExercise);
    }

    @Transactional
    @Override
    public void deleteSessionExercise(Integer id, Integer sessionId, Integer planId, Integer userId) {
        getWorkoutSessionAndValidateOwner(sessionId, planId, userId);

        SessionExercise exercise = sessionExerciseRepo.findByIdAndWorkoutSessionId(id, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session exercise not found with id: " + id));

        sessionExerciseRepo.delete(exercise);
    }

    @Transactional
    @Override
    public void deleteAllSessionExercises(Integer sessionId, Integer planId, Integer userId) {
        getWorkoutSessionAndValidateOwner(sessionId, planId, userId);
        sessionExerciseRepo.deleteAllByWorkoutSessionId(sessionId);
    }
}
