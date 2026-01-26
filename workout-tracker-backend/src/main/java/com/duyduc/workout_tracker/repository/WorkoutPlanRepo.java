package com.duyduc.workout_tracker.repository;

import com.duyduc.workout_tracker.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutPlanRepo extends JpaRepository<WorkoutPlan, Integer> {
    List<WorkoutPlan> findByUserId(Integer userId);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {
            "sessions",
            "sessions.exercises"
    })
    Optional<WorkoutPlan> findByIdAndUserId(Integer id, Integer userId);
    void deleteByUserId(Integer userId);
}
