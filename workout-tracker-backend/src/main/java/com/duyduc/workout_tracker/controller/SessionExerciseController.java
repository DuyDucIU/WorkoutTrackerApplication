package com.duyduc.workout_tracker.controller;

import com.duyduc.workout_tracker.dto.request.SessionExerciseRequest;
import com.duyduc.workout_tracker.dto.response.SessionExerciseResponse;
import com.duyduc.workout_tracker.security.UserPrincipal;
import com.duyduc.workout_tracker.service.SessionExerciseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/plans/{planId}/sessions/{sessionId}/exercises")
public class SessionExerciseController {

        private final SessionExerciseService sessionExerciseService;

        @PostMapping
        public ResponseEntity<SessionExerciseResponse> createSessionExercise(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @Valid @RequestBody SessionExerciseRequest request,
                        @AuthenticationPrincipal UserPrincipal user) {
                SessionExerciseResponse response = sessionExerciseService.createSessionExercise(request, sessionId,
                                planId,
                                user.getUserId());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @GetMapping
        public ResponseEntity<List<SessionExerciseResponse>> getSessionExercises(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @AuthenticationPrincipal UserPrincipal user) {
                List<SessionExerciseResponse> responses = sessionExerciseService.getSessionExercises(sessionId, planId,
                                user.getUserId());
                return ResponseEntity.ok(responses);
        }

        @GetMapping("/{id}")
        public ResponseEntity<SessionExerciseResponse> getSessionExerciseById(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @PathVariable("id") Integer id,
                        @AuthenticationPrincipal UserPrincipal user) {
                SessionExerciseResponse response = sessionExerciseService.getSessionExerciseById(id, sessionId, planId,
                                user.getUserId());
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}")
        public ResponseEntity<SessionExerciseResponse> updateSessionExercise(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @PathVariable("id") Integer id,
                        @Valid @RequestBody SessionExerciseRequest request,
                        @AuthenticationPrincipal UserPrincipal user) {
                SessionExerciseResponse response = sessionExerciseService.updateSessionExercise(request, id, sessionId,
                                planId,
                                user.getUserId());
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}/completed")
        public ResponseEntity<SessionExerciseResponse> markAsCompleted(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @PathVariable("id") Integer id,
                        @RequestParam("completed") Boolean completed,
                        @AuthenticationPrincipal UserPrincipal user) {
                SessionExerciseResponse response = sessionExerciseService.markAsCompleted(id, sessionId, planId,
                                user.getUserId(), completed);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteSessionExercise(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @PathVariable("id") Integer id,
                        @AuthenticationPrincipal UserPrincipal user) {
                sessionExerciseService.deleteSessionExercise(id, sessionId, planId, user.getUserId());
                return ResponseEntity.ok("Session exercise deleted!");
        }

        @DeleteMapping
        public ResponseEntity<String> deleteAllSessionExercises(
                        @PathVariable("planId") Integer planId,
                        @PathVariable("sessionId") Integer sessionId,
                        @AuthenticationPrincipal UserPrincipal user) {
                sessionExerciseService.deleteAllSessionExercises(sessionId, planId, user.getUserId());
                return ResponseEntity.ok("All session exercises deleted!");
        }
}
