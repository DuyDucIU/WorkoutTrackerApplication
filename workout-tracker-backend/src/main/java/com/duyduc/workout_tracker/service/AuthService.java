package com.duyduc.workout_tracker.service;

import com.duyduc.workout_tracker.dto.request.LoginRequest;
import com.duyduc.workout_tracker.dto.request.RegisterRequest;
import com.duyduc.workout_tracker.dto.response.AuthResponse;

public interface AuthService {
    String register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
