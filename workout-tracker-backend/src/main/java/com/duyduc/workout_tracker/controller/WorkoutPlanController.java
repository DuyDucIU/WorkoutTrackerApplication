package com.duyduc.workout_tracker.controller;

import com.duyduc.workout_tracker.dto.request.WorkoutPlanRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutPlanResponse;
import com.duyduc.workout_tracker.security.UserPrincipal;
import com.duyduc.workout_tracker.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(
            @Valid @RequestBody WorkoutPlanRequest req,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(req, user.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/copy")
    public ResponseEntity<WorkoutPlanResponse> copyWorkoutPlan(
            @PathVariable("id") Integer id,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.copyWorkoutPlan(id, user.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponse>> getWorkoutPlans(
            @AuthenticationPrincipal UserPrincipal user) {
        List<WorkoutPlanResponse> responses = workoutPlanService.getWorkoutPlans(user.getUserId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlanById(
            @PathVariable("id") Integer id,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(id, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlanById(
            @PathVariable("id") Integer id,
            @Valid @RequestBody WorkoutPlanRequest req,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlanById(req, id, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkoutPlanById(
            @PathVariable("id") Integer id,
            @AuthenticationPrincipal UserPrincipal user) {
        workoutPlanService.deleteWorkoutPlanById(id, user.getUserId());
        return ResponseEntity.ok("Workout plan deleted !");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllWorkoutPlans(
            @AuthenticationPrincipal UserPrincipal user) {
        workoutPlanService.deleteAllWorkoutPlans(user.getUserId());
        return ResponseEntity.ok("All workout plans deleted !");
    }
}
