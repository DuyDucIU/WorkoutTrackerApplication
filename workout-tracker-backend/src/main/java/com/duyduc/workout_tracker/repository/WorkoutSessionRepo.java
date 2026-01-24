package com.duyduc.workout_tracker.repository;

import com.duyduc.workout_tracker.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepo extends JpaRepository<WorkoutSession, Integer> {

    List<WorkoutSession> findByWorkoutPlanId(Integer workoutPlanId);

    Optional<WorkoutSession> findByIdAndWorkoutPlanId(Integer id, Integer workoutPlanId);

    void deleteAllByWorkoutPlanId(Integer workoutPlanId);
}
