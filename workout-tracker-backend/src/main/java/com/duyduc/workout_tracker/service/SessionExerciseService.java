package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.SessionExerciseRequest;
import com.duyduc.workout_tracker.dto.response.SessionExerciseResponse;

import java.util.List;

public interface SessionExerciseService {

    SessionExerciseResponse createSessionExercise(SessionExerciseRequest request, Integer sessionId, Integer planId,
            Integer userId);

    List<SessionExerciseResponse> getSessionExercises(Integer sessionId, Integer planId, Integer userId);

    SessionExerciseResponse getSessionExerciseById(Integer id, Integer sessionId, Integer planId, Integer userId);

    SessionExerciseResponse updateSessionExercise(SessionExerciseRequest request, Integer id, Integer sessionId,
            Integer planId, Integer userId);

    SessionExerciseResponse markAsCompleted(Integer id, Integer sessionId, Integer planId, Integer userId,
            Boolean completed);

    void deleteSessionExercise(Integer id, Integer sessionId, Integer planId, Integer userId);

    void deleteAllSessionExercises(Integer sessionId, Integer planId, Integer userId);
}
