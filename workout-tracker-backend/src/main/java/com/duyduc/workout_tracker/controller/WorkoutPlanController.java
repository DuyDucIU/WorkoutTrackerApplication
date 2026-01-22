package com.duyduc.workout_tracker.controller;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.service.WorkoutPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponse>> getWorkoutPlans() {
        List<WorkoutPlanResponse> responses = workoutPlanService.getWorkoutPlans(2);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlanById(@PathVariable("id") Integer id) {
        WorkoutPlanResponse responses = workoutPlanService.getWorkoutPlanById(id, 2);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlanById(@PathVariable("id") Integer id,
                                                                     @RequestBody WorkoutPlanRequest request ) {
        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlanById(request, id, 2);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkoutPlanById(@PathVariable("id") Integer id) {
        workoutPlanService.deleteWorkoutPlanById(id, 2);
        return ResponseEntity.ok("Workout plan deleted !");
    }
}
