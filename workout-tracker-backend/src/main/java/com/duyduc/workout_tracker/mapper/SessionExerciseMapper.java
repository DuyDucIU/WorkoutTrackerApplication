package com.duyduc.workout_tracker.mapper;

import com.duyduc.workout_tracker.dto.response.SessionExerciseResponse;
import com.duyduc.workout_tracker.entity.SessionExercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionExerciseMapper {

    @Mapping(target = "workoutSessionId", source = "workoutSession.id")
    @Mapping(target = "exerciseId", source = "exercise.id")
    @Mapping(target = "exerciseName", source = "exercise.name")
    SessionExerciseResponse toSessionExerciseResponse(SessionExercise sessionExercise);
}
