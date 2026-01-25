package com.duyduc.workout_tracker.repository;

import com.duyduc.workout_tracker.entity.SessionExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionExerciseRepo extends JpaRepository<SessionExercise, Integer> {

    List<SessionExercise> findByWorkoutSessionIdOrderByOrderIndexAsc(Integer workoutSessionId);

    Optional<SessionExercise> findByIdAndWorkoutSessionId(Integer id, Integer workoutSessionId);

    void deleteAllByWorkoutSessionId(Integer workoutSessionId);
}
