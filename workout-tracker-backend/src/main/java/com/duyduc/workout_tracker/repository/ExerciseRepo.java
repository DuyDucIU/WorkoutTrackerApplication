package com.duyduc.workout_tracker.repository;

import com.duyduc.workout_tracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepo extends JpaRepository<Exercise, Integer> {
}
