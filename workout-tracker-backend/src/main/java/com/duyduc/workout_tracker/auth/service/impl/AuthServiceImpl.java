package com.duyduc.workout_tracker.auth.service.impl;

import com.duyduc.workout_tracker.auth.dto.RegisterRequest;
import com.duyduc.workout_tracker.auth.entity.User;
import com.duyduc.workout_tracker.auth.repository.UserRepo;
import com.duyduc.workout_tracker.auth.service.AuthService;
import com.duyduc.workout_tracker.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private UserRepo userRepo;

    @Transactional
    @Override
    public String register(RegisterRequest registerRequest) {

        if (userRepo.existsByEmail(registerRequest.getEmail()))
            throw new ApiException("User existed with email: " + registerRequest.getEmail());

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        return "User registered sucessfully!!";
    }
}
