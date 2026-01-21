package com.duyduc.workout_tracker.auth.service;

import com.duyduc.workout_tracker.auth.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest registerRequest);
}
