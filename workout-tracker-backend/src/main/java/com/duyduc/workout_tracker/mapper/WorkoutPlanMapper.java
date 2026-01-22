package com.duyduc.workout_tracker.mapper;

import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.entity.WorkoutPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkoutPlanMapper {
    WorkoutPlanResponse toWorkoutPlanResponse(WorkoutPlan workoutPlan);
}
