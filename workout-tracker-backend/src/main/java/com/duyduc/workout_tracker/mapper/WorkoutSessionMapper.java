package com.duyduc.workout_tracker.mapper;

import com.duyduc.workout_tracker.dto.response.WorkoutSessionResponse;
import com.duyduc.workout_tracker.entity.WorkoutSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SessionExerciseMapper.class })
public interface WorkoutSessionMapper {

    @Mapping(target = "workoutPlanId", source = "workoutPlan.id")
    WorkoutSessionResponse toWorkoutSessionResponse(WorkoutSession workoutSession);
}
