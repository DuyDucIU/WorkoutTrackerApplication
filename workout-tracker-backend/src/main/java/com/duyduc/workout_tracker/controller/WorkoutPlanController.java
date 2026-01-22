package com.duyduc.workout_tracker.controller;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.service.WorkoutPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(@RequestBody WorkoutPlanRequest req) {
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(req, 2);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
