package com.duyduc.workout_tracker.repository;

import com.duyduc.workout_tracker.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanRepo extends JpaRepository<WorkoutPlan, Integer> {
    List<WorkoutPlan> findByUserId(Integer userId);
}
