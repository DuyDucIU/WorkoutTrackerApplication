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

    @PostMapping("/{planId}/copy")
    public ResponseEntity<WorkoutPlanResponse> copyWorkoutPlan(
            @PathVariable("planId") Integer planId,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.copyWorkoutPlan(planId, user.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponse>> getWorkoutPlans(
            @AuthenticationPrincipal UserPrincipal user) {
        List<WorkoutPlanResponse> responses = workoutPlanService.getWorkoutPlans(user.getUserId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlanById(
            @PathVariable("planId") Integer planId,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(planId, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{planId}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlanById(
            @PathVariable("planId") Integer planId,
            @Valid @RequestBody WorkoutPlanRequest req,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlanById(req, planId, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deleteWorkoutPlanById(
            @PathVariable("planId") Integer planId,
            @AuthenticationPrincipal UserPrincipal user) {
        workoutPlanService.deleteWorkoutPlanById(planId, user.getUserId());
        return ResponseEntity.ok("Workout plan deleted !");
    }
}
