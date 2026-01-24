package com.duyduc.workout_tracker.controller;

import com.duyduc.workout_tracker.dto.request.WorkoutSessionRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutSessionResponse;
import com.duyduc.workout_tracker.entity.WorkoutSessionStatus;
import com.duyduc.workout_tracker.security.UserPrincipal;
import com.duyduc.workout_tracker.service.WorkoutSessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/plans/{planId}/sessions")
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @PostMapping
    public ResponseEntity<WorkoutSessionResponse> createWorkoutSession(
            @PathVariable("planId") Integer planId,
            @RequestBody WorkoutSessionRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.createWorkoutSession(request, planId, user.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutSessionResponse>> getWorkoutSessions(
            @PathVariable("planId") Integer planId,
            @AuthenticationPrincipal UserPrincipal user) {
        List<WorkoutSessionResponse> responses = workoutSessionService.getWorkoutSessions(planId, user.getUserId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionResponse> getWorkoutSessionById(
            @PathVariable("planId") Integer planId,
            @PathVariable("id") Integer id,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.getWorkoutSessionById(id, planId, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutSessionResponse> updateWorkoutSession(
            @PathVariable("planId") Integer planId,
            @PathVariable("id") Integer id,
            @RequestBody WorkoutSessionRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.updateWorkoutSession(request, id, planId,
                user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkoutSessionResponse> updateStatus(
            @PathVariable("planId") Integer planId,
            @PathVariable("id") Integer id,
            @RequestParam("status") WorkoutSessionStatus status,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.updateStatus(id, planId, user.getUserId(), status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkoutSession(
            @PathVariable("planId") Integer planId,
            @PathVariable("id") Integer id,
            @AuthenticationPrincipal UserPrincipal user) {
        workoutSessionService.deleteWorkoutSession(id, planId, user.getUserId());
        return ResponseEntity.ok("Workout session deleted!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllWorkoutSessions(
            @PathVariable("planId") Integer planId,
            @AuthenticationPrincipal UserPrincipal user) {
        workoutSessionService.deleteAllWorkoutSessions(planId, user.getUserId());
        return ResponseEntity.ok("All workout sessions deleted!");
    }
}
