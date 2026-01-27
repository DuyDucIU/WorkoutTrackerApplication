package com.duyduc.workout_tracker.controller;

import com.duyduc.workout_tracker.dto.request.WorkoutSessionRequest;
import com.duyduc.workout_tracker.dto.response.WorkoutSessionResponse;
import com.duyduc.workout_tracker.entity.WorkoutSessionStatus;
import com.duyduc.workout_tracker.security.UserPrincipal;
import com.duyduc.workout_tracker.service.WorkoutSessionService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody WorkoutSessionRequest request,
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

    @GetMapping("/{sessionId}")
    public ResponseEntity<WorkoutSessionResponse> getWorkoutSessionById(
            @PathVariable("planId") Integer planId,
            @PathVariable("sessionId") Integer sessionId,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.getWorkoutSessionById(sessionId, planId, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<WorkoutSessionResponse> updateWorkoutSession(
            @PathVariable("planId") Integer planId,
            @PathVariable("sessionId") Integer sessionId,
            @Valid @RequestBody WorkoutSessionRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.updateWorkoutSession(request, sessionId, planId,
                user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<WorkoutSessionResponse> updateStatus(
            @PathVariable("planId") Integer planId,
            @PathVariable("sessionId") Integer sessionId,
            @RequestParam("status") WorkoutSessionStatus status,
            @AuthenticationPrincipal UserPrincipal user) {
        WorkoutSessionResponse response = workoutSessionService.updateStatus(sessionId, planId, user.getUserId(), status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<String> deleteWorkoutSession(
            @PathVariable("planId") Integer planId,
            @PathVariable("sessionId") Integer sessionId,
            @AuthenticationPrincipal UserPrincipal user) {
        workoutSessionService.deleteWorkoutSession(sessionId, planId, user.getUserId());
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
